package com.app.feenix.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.feenix.BuildConfig
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.eventbus.NoDriversFoundModel
import com.app.feenix.eventbus.RideAcceptModel
import com.app.feenix.utils.Log
import com.app.feenix.view.ui.ConfirmPickupLocationActivity
import com.app.feenix.view.ui.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus
import org.json.JSONException

class FcmService : FirebaseMessagingService() {

    var myPreference: MyPreference? = null
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        myPreference = MyPreference(this)
        myPreference?.fcmToken = token
        Log.error("FCMTOken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
            if(message.data.get("trip").equals("Ride"))
            {
                if(message.data.get("type").equals("No Driver") &&  myPreference?.TripSearchingStatus.equals("false"))
                {
                    EventBus.getDefault().postSticky(NoDriversFoundModel(message.data.get("type"),
                        message.data.get("title"),message.data.get("message"),message.data.get("details")))
                }
                else
                {
                    myPreference?.TripSearchingStatus= "false"
                    EventBus.getDefault().postSticky(RideAcceptModel(message))
                }
            }
        sendNotification(message.data.get("title"),message.data.get("message"),message.data.get("type"),message)
        android.util.Log.e("changescalled", "" + message.data.toString())
        android.util.Log.e("changescalled", "" + myPreference?.TripSearchingStatus)
    }

    var attributes: AudioAttributes? = null
    private var notifManager: NotificationManager? = null
    @SuppressLint("SuspiciousIndentation")
    @Throws(JSONException::class)
    private fun sendNotification(title: String?, message: String?, type: String?, remoteMessage: RemoteMessage) {
        var intent: Intent ?=null
        val NOTIFY_ID = 1002
            if (type.equals("No Driver", ignoreCase = true)&& myPreference?.TripSearchingStatus.equals("false")) {
                intent = Intent(this, ConfirmPickupLocationActivity::class.java)
            }
          else{
                intent = Intent(this, HomeActivity::class.java)
            }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val name = "my_package_channel"
        val id = "my_package_channel_1" // The user-visible name of the channel.
        val description = "my_package_first_channel" // The user-visible description of the channel.
        val builder: NotificationCompat.Builder
        if (notifManager == null) {
            notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        val sound: Uri

        sound = if (remoteMessage.data.get("type").equals("Arrived", ignoreCase = true)) {
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.car_parking)
        } else {
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.normal_notification)
        }
        attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val importance = NotificationManager.IMPORTANCE_HIGH
        var mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableVibration(false)
        mChannel.lightColor = Color.GREEN
        mChannel.setSound(sound, attributes)
        notifManager!!.createNotificationChannel(mChannel)
        builder = NotificationCompat.Builder(this, id)
        builder.setContentTitle(title).setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentText(message) // required
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.img_notification_header)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setTicker(message)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            builder.addAction(
                NotificationCompat.Action.Builder(
                    0,
                    this.getString(R.string.enable_label),
                    pendingIntent
                ).build()
            )
        }
        val notification = builder.build()
        notifManager?.notify(NOTIFY_ID, notification)
    }
}
package com.app.feenix.view.activities.notification

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.ResponseModel.NotificationData
import com.app.biu.model.ResponseModel.NotificationResponse
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityNotificationBinding
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.view.adapter.NotificationAdapter
import com.app.feenix.viewmodel.INotificationData
import com.app.feenix.webservices.notification.NotificationService

class NotificationActivity : BaseActivity(), View.OnClickListener, INotificationData {


    private lateinit var binding: ActivityNotificationBinding
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private var notificationService: NotificationService? = null
    private lateinit var mNotificationlist: ArrayList<NotificationData>
    var mAdapter: NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallback()
        initRecyclerview()
    }

    private fun initRecyclerview() {

        binding.rvNotifications.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvNotifications.adapter = mAdapter
        binding.rvNotifications.isNestedScrollingEnabled
    }

    private fun initCallback() {

        binding.imgNotificationBack.setOnClickListener(this)

    }


    private fun initObjects() {
        mContext = this@NotificationActivity
        myPreference = MyPreference(mContext!!)
        notificationService = NotificationService()
        notificationService?.NotificationService(mContext!!)

        mNotificationlist = ArrayList()
        mAdapter = NotificationAdapter(mContext!!, mNotificationlist)
        if (hasInternetConnection()) {
            notificationService?.getNotification(this)
        } else {
            ToastBuilder.build(mContext, "Please connect internet and try again")
        }

    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_notification_back -> {
                onBackPressed()
            }

        }
    }

    override fun ongetNotifications(notificationResponse: NotificationResponse) {
        if (notificationResponse.notifications.size > 0) {
            binding.emptyLayout.visibility = View.GONE
            binding.rvNotifications.visibility = View.VISIBLE
            mNotificationlist.clear()
            mNotificationlist.addAll(notificationResponse.notifications)
            mAdapter!!.notifyDataSetChanged()
        } else {
            binding.emptyLayout.visibility = View.VISIBLE
            binding.rvNotifications.visibility = View.GONE
        }

    }


}
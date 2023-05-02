package com.app.feenix.view.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.MyActivity.launchWithBundle
import com.app.feenix.BuildConfig
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityHomeBinding
import com.app.feenix.utils.Log
import com.app.feenix.view.ui.Walkthrough.WalkthroughActivity
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.view.ui.notification.NotificationActivity
import com.app.feenix.view.ui.promotions.PromotionsActivity
import com.app.feenix.view.ui.referAndearn.ReferAndEarnActivity
import com.app.feenix.view.ui.tripdetails.YourTripsActivity
import com.app.feenix.view.ui.wallet.WalletActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import io.intercom.android.sdk.identity.Registration

class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

    var mContext: Context? = null
    private var myPreference: MyPreference? = null

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    var ic_menu: ImageView? = null
    lateinit var homeDrawer: LinearLayout
    lateinit var yourTripsDrawer: LinearLayout
    lateinit var communityDrawer: LinearLayout
    lateinit var walletDrawer: LinearLayout
    lateinit var promotionsDrawer: LinearLayout
    lateinit var notificationDrawer: LinearLayout
    lateinit var referAndEarnDrawer: LinearLayout
    lateinit var helpDrawer: LinearLayout
    lateinit var logoutDrawer: LinearLayout
    lateinit var bottomDrawer: LinearLayout
    lateinit var profileDrawer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        initCallbacks()
        prepareObjects()
        setupDrawer()
        initSetHomeProfile()
    }

    private fun initSetHomeProfile() {

        Glide.with(mContext!!).load(myPreference?.profilePic).into(binding.navviewLayout.userPic)
        binding.navviewLayout.userName.text = myPreference?.Username
    }

    private fun initCallbacks() {
        ic_menu?.setOnClickListener(this)
        homeDrawer.setOnClickListener(this)
        yourTripsDrawer.setOnClickListener(this)
        communityDrawer.setOnClickListener(this)
        walletDrawer.setOnClickListener(this)
        promotionsDrawer.setOnClickListener(this)
        notificationDrawer.setOnClickListener(this)
        referAndEarnDrawer.setOnClickListener(this)
        helpDrawer.setOnClickListener(this)
        logoutDrawer.setOnClickListener(this)
        profileDrawer.setOnClickListener(this)
        bottomDrawer.setOnClickListener(this)
    }


    private fun initObject() {
        mContext = this@HomeActivity
        myPreference = MyPreference(mContext!!)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        ic_menu = binding.menuLayout.imgHomeMenu
        homeDrawer = binding.navviewLayout.homeDrawer
        yourTripsDrawer = binding.navviewLayout.yourTripsDrawer
        communityDrawer = binding.navviewLayout.communityDrawer
        walletDrawer = binding.navviewLayout.walletDrawer
        promotionsDrawer = binding.navviewLayout.couponDrawer
        notificationDrawer = binding.navviewLayout.NoficationDrawer
        referAndEarnDrawer = binding.navviewLayout.shareDrawer
        helpDrawer = binding.navviewLayout.helpDeskDrawer
        logoutDrawer = binding.navviewLayout.logoutDrawer
        profileDrawer = binding.navviewLayout.profileDrawer
        bottomDrawer = binding.navviewLayout.bottomLayoutDrive

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.drawer_open, R.string.drawer_close
        )
        mDrawerToggle!!.isDrawerIndicatorEnabled = false

        Log.error("fjdfpreef", myPreference?.userToken!!)
        val registration = Registration.create().withUserId(myPreference?.firstName!!)
        Intercom.client().registerIdentifiedUser(registration)

        sendCustomDetails()
    }

    // Send Custom details via Intercom
    private fun sendCustomDetails() {
        val deviceversion = Build.VERSION.RELEASE
        val Devicename = Build.MANUFACTURER
        val userAttributes: UserAttributes = UserAttributes.Builder()
            .withCustomAttribute("first_name", myPreference?.firstName)
            .withCustomAttribute("last_name", myPreference?.lastName)
            .withCustomAttribute("mobile", myPreference?.mobile)
            .withCustomAttribute("email", myPreference?.email)
            .withCustomAttribute("device_type", "android")
            .withCustomAttribute("device_token", myPreference?.fcmToken)
            .withCustomAttribute("version", BuildConfig.VERSION_CODE)
            .withCustomAttribute("device_name", Devicename)
            .withCustomAttribute("device_android_version", deviceversion)
            .withCustomAttribute("app_name", "Feenix User")
            .withCustomAttribute("user_id", myPreference?.id)
            .withCustomAttribute("no_of_bookings", myPreference?.TotalRequest)
            .withCustomAttribute("no_of_bookings_cancelled", myPreference?.CancelledRequest)
            .withCustomAttribute("no_of_bookings_completed", myPreference?.CompletedRequest)
            .withCustomAttribute("last_booking_date", myPreference?.LastBookingDate)
            .withCustomAttribute("last_booking_status", myPreference?.LastBookingStatus)
            .build()
        Intercom.client().updateUser(userAttributes)

    }

    private fun prepareObjects() {

        drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })


    }

    private fun setupDrawer() {
        drawerLayout!!.addDrawerListener(mDrawerToggle!!)
        mDrawerToggle!!.syncState()
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.img_home_menu -> {
                drawerLayout?.openDrawer(GravityCompat.START)
            }
            R.id.homeDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
            }
            R.id.your_trips_Drawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launch(mContext!!, YourTripsActivity::class.java)
            }
            R.id.profileDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launch(mContext!!, ProfileActivity::class.java)
            }
            R.id.walletDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launch(mContext!!, WalletActivity::class.java)
            }
            R.id.couponDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                val bundle = Bundle()
                bundle.putString("Type", "1")
                launchWithBundle(mContext!!, PromotionsActivity::class.java, bundle)
            }
            R.id.NoficationDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launch(mContext!!, NotificationActivity::class.java)
            }
            R.id.shareDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launch(mContext!!, ReferAndEarnActivity::class.java)
            }
            R.id.logoutDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                MyActivity.launchClearStack(mContext!!, WalkthroughActivity::class.java)
                myPreference?.clearUser()
            }
            R.id.helpDeskDrawer -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                helpDesk()
            }
            R.id.bottom_layout_drive -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data =
                    Uri.parse("https://play.google.com/store/apps/details?id=com.app.feenixdriver")
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Intercom.client().handlePushMessage()
    }

    private fun helpDesk() {

        Intercom.client().displayConversationsList()
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
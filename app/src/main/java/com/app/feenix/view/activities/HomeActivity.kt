package com.app.feenix.view.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cbs.com.bmr.Utilities.MyActivity
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityHomeBinding
import com.app.feenix.view.activities.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView

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
            }
            R.id.profileDrawer -> {
                MyActivity.launch(mContext!!, ProfileActivity::class.java)
            }
        }
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
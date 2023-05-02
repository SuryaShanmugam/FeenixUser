package com.app.feenix.view.ui.tripdetails

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.feenix.R
import com.app.feenix.databinding.ActivityYourTripsBinding
import com.app.feenix.view.adapter.MyPagerAdapter
import com.app.feenix.view.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayout


class YourTripsActivity : BaseActivity(), TabLayout.OnTabSelectedListener, View.OnClickListener {


    private lateinit var binding: ActivityYourTripsBinding
    var mContext: Context? = null

    private var mTabLayout: TabLayout? = null
    var mViewPager: ViewPager? = null
    private var mPagerAdapter: MyPagerAdapter? = null
    private val mFragmentList = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        iniCallbacks()
        initTabs()
    }

    private fun initTabs() {

        mTabLayout!!.addTab(mTabLayout!!.newTab().setText("Ride"))
        mFragmentList.add(RideTripsFragment())
        mTabLayout?.newTab()?.setText("Delivery")?.let { mTabLayout?.addTab(it) }
        mFragmentList.add(DeliveryTripsFragment())
        mPagerAdapter?.notifyDataSetChanged()
        mViewPager?.adapter = mPagerAdapter
        mViewPager?.offscreenPageLimit = 0
        mViewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
    }

    private fun iniCallbacks() {

        mTabLayout!!.addOnTabSelectedListener(this)
        binding.imgBackYourtrips.setOnClickListener(this)
    }

    private fun initObjects() {
        mContext = this@YourTripsActivity
        mTabLayout = binding.tabYourtrips
        mViewPager = binding.viewPagerYourtrips
        mPagerAdapter = MyPagerAdapter(supportFragmentManager, mFragmentList)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        mViewPager?.currentItem = tab?.position!!

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onClick(v: View?) {
        val id= v?.id
        when(id)
        {
            R.id.img_back_yourtrips->
            {
                onBackPressed()
            }
        }
    }
}
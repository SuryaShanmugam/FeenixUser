package com.app.feenix.view.ui.referAndearn

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.feenix.R
import com.app.feenix.databinding.ActivityReferearnBinding
import com.app.feenix.view.adapter.MyPagerAdapter
import com.app.feenix.view.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayout

class ReferAndEarnActivity : BaseActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {
    private lateinit var binding: ActivityReferearnBinding
    var mContext: Context? = null

    private var mTabLayout: TabLayout? = null
    var mViewPager: ViewPager? = null
    private var mPagerAdapter: MyPagerAdapter? = null
    private val mFragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferearnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallbacks()
        initTabs()
    }


    private fun initCallbacks() {
        binding.imgBackReferearn.setOnClickListener(this)
        mTabLayout!!.addOnTabSelectedListener(this)
    }

    private fun initObjects() {
        mContext = this@ReferAndEarnActivity
        mTabLayout = binding.tabReferearn
        mViewPager = binding.viewPagerReferearn
        mPagerAdapter = MyPagerAdapter(supportFragmentManager, mFragmentList)

    }

    private fun initTabs() {

        mTabLayout!!.addTab(mTabLayout!!.newTab().setText("Invite"))
        mFragmentList.add(InviteFragment())
        mTabLayout?.newTab()?.setText("Earnings")?.let { mTabLayout?.addTab(it) }
        mFragmentList.add(EarningsFragment())
        mPagerAdapter?.notifyDataSetChanged()
        mViewPager?.adapter = mPagerAdapter
        mViewPager?.offscreenPageLimit = 0
        mViewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
    }


    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_back_referearn -> {
                onBackPressed()
            }


        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        mViewPager?.currentItem = tab?.position!!

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }


}
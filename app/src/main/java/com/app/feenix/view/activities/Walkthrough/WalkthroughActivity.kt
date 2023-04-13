package com.app.feenix.view.activities.Walkthrough

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import cbs.com.bmr.Utilities.MyActivity
import com.app.feenix.R
import com.app.feenix.databinding.ActivityWalkthroughBinding
import com.app.feenix.feature.internet.InternetConnectionLayout
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.view.activities.signin.SignInMobileActivity
import com.app.feenix.view.adapter.MyPagerAdapter
import com.rd.PageIndicatorView

class WalkthroughActivity : BaseActivity(), View.OnClickListener,
    InternetConnectionManager.InternetConnectionListener {

    private lateinit var binding: ActivityWalkthroughBinding
    private var mPagerIntro: ViewPager? = null
    private var mIndicatorView: PageIndicatorView? = null
    private var mFragmentList: ArrayList<WalkthroughFragment>? = null
    private var mPagerAdapter: MyPagerAdapter? = null
    private var mCardSignIn: CardView? = null
    var mContext: Context? = null
    private lateinit var internetConnectionLayout: InternetConnectionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        internetConnectionLayout = binding.activityWalkthroughInternetConnectionLayout.root
        internetConnectionLayout.init(this)
        initObjects()
        initCallbacks()
        populateIntro()
        initViewPager()
    }

    private fun initCallbacks() {
        mCardSignIn?.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        internetConnectionLayout.apply {
            onResume()
            registerInternetConnectionListener("WalkthroughActivity")
        }

    }

    override fun onPause() {
        super.onPause()
        internetConnectionLayout.unregisterInternetConnectionListener()
    }

    override fun onInternetAvailable() {

        internetConnectionLayout.onInternetAvailable()

    }

    override fun onInternetLost() {
        internetConnectionLayout.onInternetLost()
    }

    private fun initObjects() {
        mContext = this@WalkthroughActivity
        mPagerIntro = binding.pagerIntro
        mIndicatorView = binding.indicator
        mCardSignIn = binding.cardSignin
        mFragmentList = ArrayList()
        mPagerAdapter = MyPagerAdapter(supportFragmentManager, mFragmentList)

    }

    private fun initViewPager() {
        mPagerIntro!!.adapter = mPagerAdapter
        mIndicatorView!!.setViewPager(mPagerIntro)
    }

    private fun populateIntro() {
        mFragmentList?.add(
            WalkthroughFragment.newInstance(
                "Easy to Use", "Simple and easy to order a ride",
                "Skip", R.drawable.ic_walkthrough_easy_search
            )
        )
        mFragmentList?.add(
            WalkthroughFragment.newInstance(
                "Get a ride in minutes",
                "Over 1,000 drivers joining every month to \n \n drive you to your destination.",
                "Skip",
                R.drawable.ic_walkthrough_bestroute
            )
        )
        mFragmentList?.add(
            WalkthroughFragment.newInstance(
                "Enjoy Low Fares",
                "Cover more distance for less with Feenix", "Get Started",
                R.drawable.ic_walkthrough_enjoy
            )
        )
        mPagerAdapter!!.notifyDataSetChanged()
        mIndicatorView!!.count = mFragmentList!!.size
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.card_signin -> {
                MyActivity.launch(mContext!!, SignInMobileActivity::class.java)
            }
        }
    }

}
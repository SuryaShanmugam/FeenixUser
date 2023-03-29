package com.app.feenix.view.activities

import android.os.Bundle
import com.app.feenix.databinding.ActivityWalkthroughBinding
import com.app.feenix.feature.internet.InternetConnectionLayout
import com.app.feenix.feature.internet.InternetConnectionManager
import com.app.feenix.view.base.BaseActivity

class WalkthroughActivity : BaseActivity(), InternetConnectionManager.InternetConnectionListener {


    private lateinit var binding: ActivityWalkthroughBinding
    private lateinit var internetConnectionLayout: InternetConnectionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        internetConnectionLayout = binding.activityWalkthroughInternetConnectionLayout.root
        internetConnectionLayout.init(this)
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
}
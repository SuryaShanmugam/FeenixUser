package com.app.feenix.feature.internet

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.feenix.R
import com.app.feenix.databinding.InflaterInternetConnectionInfoLayoutBinding
import com.app.feenix.view.ui.base.BaseActivity


class InternetConnectionLayout(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs), InternetConnectionManager.InternetConnectionListener {

    private lateinit var activity: BaseActivity
    private lateinit var statusTv: TextView
    private var connectionTag: String = ""
    private lateinit var binding: InflaterInternetConnectionInfoLayoutBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = InflaterInternetConnectionInfoLayoutBinding.bind(this)
        statusTv = binding.inflaterInternetConnectionInfoLayoutTv
    }

    fun init(baseActivity: BaseActivity) {
        activity = baseActivity
    }

    fun onResume() {
        if (InternetConnectionManager.getInstance().hasInternetConnection(context)) {
            onInternetAvailable()
        } else {
            onInternetLost()
        }
    }

    fun registerInternetConnectionListener(tag: String) {
        connectionTag = tag
        InternetConnectionManager.getInstance().addInternetConnectionListener(tag, this)
    }

    fun registerInternetConnectionListener(
        tag: String,
        internetConnectionListener: InternetConnectionManager.InternetConnectionListener
    ) {
        connectionTag = tag
        InternetConnectionManager.getInstance()
            .addInternetConnectionListener(tag, internetConnectionListener)
    }

    fun unregisterInternetConnectionListener() {
        InternetConnectionManager.getInstance().removeInternetConnectionListener(connectionTag)
    }

    private val delayRunnable = Runnable {
        slideViewUp()
    }

    override fun onInternetAvailable() {
        activity.runOnUiThread {
            Log.d("InternetConn", "on available in icl")
            if (visibility == View.VISIBLE) {
                Log.d("dsdfsfdssfrtrg", "on available in icl")

                statusTv.apply {
                    setBackgroundColor(resources.getColor(R.color.acceptGreen, null))
                    text = resources.getString(R.string.internet_available)
                }
                postDelayed(delayRunnable, 2000)
            }
        }
    }

    override fun onInternetLost() {
        activity.runOnUiThread {
            removeCallbacks(delayRunnable)
            Log.d("InternetConn", "" + statusTv.text.toString())
            if (statusTv.text != resources.getString(R.string.internet_lost)) {
                Log.d("InternetConn", "on lost in gfdgd")
                visibility = View.VISIBLE
                statusTv.apply {
                    setBackgroundColor(resources.getColor(R.color.acceptRed, null))
                    text = resources.getString(R.string.internet_lost)
                }
                slideViewDown()
            }
        }
    }

    private fun slideViewUp() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //Do Nothing
            }

            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                //Do Nothing
            }

        })
        startAnimation(anim)
    }

    private fun slideViewDown() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //Do Nothing
            }

            override fun onAnimationEnd(animation: Animation?) {
                //Do Nothing
            }

            override fun onAnimationStart(animation: Animation?) {
                visibility = View.VISIBLE
            }

        })
        startAnimation(anim)
    }
}
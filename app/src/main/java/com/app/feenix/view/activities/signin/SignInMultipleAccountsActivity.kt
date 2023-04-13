package com.app.feenix.view.activities.signin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import cbs.com.bmr.Utilities.MyActivity
import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySignInMultipleAccountsBinding
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.viewmodel.ISignInMobile
import com.app.feenix.webservices.SignIn.SignInService
import com.hellotirupathur.utils.TextChangedListener

class SignInMultipleAccountsActivity : BaseActivity(), View.OnClickListener,ISignInMobile {

    private lateinit var binding: ActivitySignInMultipleAccountsBinding
    var mContext: Context? = null
    private var authService: SignInService? = null
    var MobileNumber:String?=null
    var CountryCode:String?=null
    private var myPreference: MyPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInMultipleAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        processbundle()
        initObject()
        initCallback()
    }

    private fun processbundle() {
        val bundle= intent.extras
        MobileNumber = bundle?.getString("phoneNumber")
        CountryCode = bundle?.getString("CountryCode")
    }

    private fun initCallback() {
        binding.signInMultiple.setOnClickListener(this)
        binding.imgBackMultiple.setOnClickListener(this)
    }

    private fun initObject() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        mContext = this@SignInMultipleAccountsActivity
        myPreference = MyPreference(mContext!!)
        authService = SignInService()
        authService!!.SignInService(this@SignInMultipleAccountsActivity)
        TextChangedListener.onTextChanged(binding.emailIdMultiple,binding.signInMultiple)


    }

    override fun onClick(v: View?) {
        val id = v?.id

        when (id)
        {
            R.id.signIn_multiple->
            {

                authService?.getLoginMobile(
                    this, MobileNumber!!,CountryCode!!,binding.emailIdMultiple.text.toString()

                )
            }
            R.id.img_back_multiple->
            {
                onBackPressed()
            }
        }
    }

    override fun onSignInMobileResponse(signInMobileResponse: SignInMobileResponse) {
        if (signInMobileResponse.success!!) {
            myPreference!!.token = signInMobileResponse.data?.access_token
            myPreference!!.Username = signInMobileResponse.data?.first_name+signInMobileResponse.data?.last_name
            myPreference!!.email = signInMobileResponse.data?.email
            myPreference!!.mobile = CountryCode+MobileNumber
            val bundle = Bundle()
            bundle.putString("phoneNumber", MobileNumber)
            bundle.putString("CountryCode", CountryCode)
            bundle.putInt("new", signInMobileResponse.data?.new!!.toInt())
            bundle.putString("first_name", signInMobileResponse.data.first_name)
            bundle.putString("last_name", signInMobileResponse.data.last_name)
            MyActivity.launchWithBundle(mContext!!, SignInNameActivity::class.java, bundle)
        } else {
            val bundle = Bundle()
            bundle.putString("phoneNumber", MobileNumber)
            bundle.putString("CountryCode", CountryCode)
            MyActivity.launchWithBundle(mContext!!, SignInVerifyPhoneActivity::class.java, bundle)
        }


    }
}
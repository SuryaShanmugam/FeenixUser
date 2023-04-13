package com.app.feenix.view.activities.signin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySignInEmailBinding
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.SignIn.SignInService
import com.hellotirupathur.utils.TextChangedListener
import com.hellotirupathur.utils.Validator

class SignInEmailActivity : BaseActivity() , IUpdateProfile, View.OnClickListener {

    private lateinit var binding: ActivitySignInEmailBinding
    var mContext: Context? = null
    private var authService: SignInService? = null

    private var myPreference: MyPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallbacks()
    }
    private fun initCallbacks() {
        binding.signInEmail.setOnClickListener(this)
        binding.imgBackEmail.setOnClickListener(this)
        binding.txtSkip.setOnClickListener(this)
    }

    private fun initObjects() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        mContext = this@SignInEmailActivity
        myPreference = MyPreference(mContext!!)

        TextChangedListener.onTextChanged(binding.emailIdSign,binding.signInEmail)

        authService = SignInService()
        authService!!.SignInService(this@SignInEmailActivity)
    }

    override fun onClick(v: View?) {
        val id= v?.id
        when(id)
        {
            R.id.signIn_email->
            {
                if(Validator.isValidationEditext(binding.emailIdSign,"Enter Email"))
                {
                    authService?.UpdateUserEmail(this,
                        binding.emailIdSign.text.toString().trim())
                }

            }
            R.id.txt_skip->
            {
                MyActivity.launchClearTop(mContext!!,SignInProfileActivity::class.java)
            }
            R.id.img_back_email->
            {
                onBackPressed()
            }
        }

    }
    override fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if(updateProfileMobileResponse.success)
        {
            myPreference!!.email = updateProfileMobileResponse.data?.email
            MyActivity.launch(mContext!!,SignInProfileActivity::class.java)
        }
        else
        {
            ToastBuilder.build(mContext!!,"Response Error")
        }

    }
}
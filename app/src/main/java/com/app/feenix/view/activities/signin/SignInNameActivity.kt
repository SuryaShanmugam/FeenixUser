package com.app.feenix.view.activities.signin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySignInNameBinding
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.SignIn.SignInService
import com.hellotirupathur.utils.TextChangedListener
import com.hellotirupathur.utils.Validator

class SignInNameActivity : BaseActivity() , View.OnClickListener,IUpdateProfile {

    private lateinit var binding: ActivitySignInNameBinding
    var mContext: Context? = null
    private var authService: SignInService? = null
    private var myPreference: MyPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallbacks()
    }


    private fun initCallbacks() {
        binding.signInName.setOnClickListener(this)
        binding.imgBackName.setOnClickListener(this)
    }

    private fun initObjects() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        mContext = this@SignInNameActivity
        TextChangedListener.onTextChanged(binding.editFirstname,binding.signInName)
        TextChangedListener.onTextChanged(binding.editLastname,binding.signInName)
        myPreference = MyPreference(mContext!!)

        authService = SignInService()
        authService!!.SignInService(this@SignInNameActivity)
    }

    override fun onClick(v: View?) {
        val id= v?.id
        when(id)
        {
            R.id.signIn_name->
            {
                if(
                    Validator.isValidationEditext(binding.editFirstname,"Enter FirstName")&&
                    Validator.isValidationEditext(binding.editLastname,"Enter LastName"))
                {
                    authService?.UpdateUserName(this,
                        binding.editFirstname.text.toString().trim(),
                        binding.editLastname.text.toString().trim())
                }

            }
            R.id.img_back_name->
            {
                onBackPressed()
            }
        }

    }

    override fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if(updateProfileMobileResponse.success) {
            myPreference?.firstName = updateProfileMobileResponse.data?.first_name
            myPreference?.lastName = updateProfileMobileResponse.data?.last_name
            MyActivity.launch(mContext!!, SignInEmailActivity::class.java)
        }
        else
        {
            ToastBuilder.build(mContext!!,"Response Error")
        }
    }
}
package com.app.feenix.view.ui.signin

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
import com.app.feenix.view.ui.HomeActivity
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.SignIn.SignInService
import com.hellotirupathur.utils.TextChangedListener
import com.hellotirupathur.utils.Validator

class SignInEmailActivity : BaseActivity(), IUpdateProfile, View.OnClickListener, IGetProfileData {

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
                authService?.getProfileDetails(this)
            }
            R.id.img_back_email->
            {
                onBackPressed()
            }
        }

    }
    override fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if(updateProfileMobileResponse.success) {
            myPreference!!.email = updateProfileMobileResponse.data?.email
            MyActivity.launch(mContext!!, SignInProfileActivity::class.java)
        } else {
            ToastBuilder.build(mContext!!, "Response Error")
        }

    }

    override fun onGetProfileResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {

        if (updateProfileMobileResponse.success) {
            myPreference?.dynamicMapkey = updateProfileMobileResponse.data?.android_user_mapkey
            myPreference?.email = updateProfileMobileResponse.data?.email
            myPreference?.firstName = updateProfileMobileResponse.data?.first_name
            myPreference?.lastName = updateProfileMobileResponse.data?.last_name
            myPreference?.mobile = updateProfileMobileResponse.data?.mobile
            myPreference?.countryCode = updateProfileMobileResponse.data?.country_code
            myPreference?.ReferralCode = updateProfileMobileResponse.data?.referal
            myPreference?.id = updateProfileMobileResponse.data?.id!!
            myPreference?.welcomeImage = updateProfileMobileResponse.data.welcome_image
            myPreference?.sosNumber = updateProfileMobileResponse.data.sos_number
            myPreference?.profilePic = updateProfileMobileResponse.data.picture
            myPreference?.fleet = updateProfileMobileResponse.data.fleet
            myPreference?.walletBal = updateProfileMobileResponse.data.wallet_balance
            myPreference?.TotalRequest = updateProfileMobileResponse.data.total_request
            myPreference?.CancelledRequest = updateProfileMobileResponse.data.cancelled_request
            myPreference?.CompletedRequest = updateProfileMobileResponse.data.completed_request
            myPreference?.LastBookingStatus = updateProfileMobileResponse.data.last_trip_status
            myPreference?.LastBookingDate = updateProfileMobileResponse.data.last_booking_date?.date
            moveHomeActivity(
                updateProfileMobileResponse.data.active_request_flow,
                updateProfileMobileResponse.data.active_request_id
            )

        } else {
            ToastBuilder.build(mContext!!, "Response Error")
        }

    }

    private fun moveHomeActivity(activeRequestFlow: String, activeRequestId: String) {

        MyActivity.launchClearStack(mContext!!, HomeActivity::class.java)

    }

}
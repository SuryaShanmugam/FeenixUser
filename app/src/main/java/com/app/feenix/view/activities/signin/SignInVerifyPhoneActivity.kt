package com.app.feenix.view.activities.signin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.RequestModel.ResponseModel.SignInVerifyOTPResponse
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.broadcastreceiver.SmsBroadcastReceiver
import com.app.feenix.databinding.ActivitySignInVerifyPhoneBinding
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.viewmodel.ISignInVerifyOtp
import com.app.feenix.viewmodel.OtpReceivedInterface
import com.app.feenix.webservices.SignIn.SignInService
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task


class SignInVerifyPhoneActivity : BaseActivity() , OtpReceivedInterface,ISignInVerifyOtp,
View.OnClickListener,IGetProfileData{

    private lateinit var binding: ActivitySignInVerifyPhoneBinding
    var mContext: Context? = null
    var mSmsBroadcastReceiver: SmsBroadcastReceiver? = null
    private var authService: SignInService? = null
    var phoneNumber:String?=null
    var MobileNumber:String?=null
    var CountryCode:String?=null
    var new:Int?=null
    var first_name:String?=null
    var last_name:String?=null
    private var myPreference: MyPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInVerifyPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        processBundle()
        initObject()
        initSMSListner()
        initCallbacks()
        startSMSRetrieverClient()
        intiEditKeyListener()
    }

    private fun initSMSListner() {
        mSmsBroadcastReceiver = SmsBroadcastReceiver()
        mSmsBroadcastReceiver!!.setOnOtpListeners(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(mSmsBroadcastReceiver, intentFilter)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }



    private fun processBundle() {
        val bundle= intent.extras
        phoneNumber = bundle?.getString("CountryCode") +bundle?.getString("phoneNumber")
        MobileNumber = bundle?.getString("phoneNumber")
        CountryCode = bundle?.getString("CountryCode")
        first_name = bundle?.getString("first_name")
        last_name = bundle?.getString("last_name")
        new = bundle?.getInt("new")
        binding.textTitleNo.setText(String.format("%s", phoneNumber))



    }
    private fun initCallbacks() {

        binding.signInVerifyotp.setOnClickListener(this)
        binding.txtResendOtp.setOnClickListener(this)
        binding.txtVerifyLater.setOnClickListener(this)
        binding.imgBackArrow.setOnClickListener(this)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onClick(v: View?) {
     val id = v?.id
        when(id)
        {
            R.id.img_back_arrow->
            {
                    onBackPressed()
            }
            R.id.txt_verify_later-> {
                if (new == 1) {
                    MyActivity.launch(mContext!!,SignInNameActivity::class.java)
                } else if (new == 0) {
                    if (first_name.isNullOrEmpty() && last_name.isNullOrEmpty()) {
                        MyActivity.launch(mContext!!,SignInNameActivity::class.java)
                    } else {
                        authService?.getProfileDetails(this)
                    }
                }
            }
            R.id.txt_resend_otp->
            {
                authService?.SendOtp(this, phoneNumber!!, "ResendOtp")
            }
            R.id.signIn_verifyotp->
            {
                val code1: String = binding.edit1.text.toString().trim()
                val code2: String = binding.edit2.text.toString().trim()
                val code3: String = binding.edit3.text.toString().trim()
                val code4: String = binding.edit4.text.toString().trim()
                val code = code1 + code2 + code3 + code4
                processOtp(code)
            }
        }
    }

    private fun processOtp(code: String) {
        if (!resOtpCode.isNullOrEmpty()) {
            if (resOtpCode == code) {
                authService?.UpdatePhoneNumber(
                  this,MobileNumber,CountryCode)
            } else {
                ToastBuilder.build(mContext!!,"Invalid OTP")
            }
        } else {
            ToastBuilder.build(mContext!!,"Invalid OTP")

        }
    }
    private fun initObject() {
        mContext = this@SignInVerifyPhoneActivity
        myPreference = MyPreference(mContext!!)
        authService = SignInService()
        authService!!.SignInService(this@SignInVerifyPhoneActivity)
        authService?.SendOtp(this, phoneNumber!!, "SendOtp")
    }

    override fun onOtpReceived(otp: String?) {

        if (otp!!.contains("[#] Feenix: Your mobile verification code is ")) {
            // Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();
            val otpMessage = otp.replace("[#] Feenix: Your mobile verification code is ", "")
            val otpFormated = otpMessage.replace(
                ". If your app has 6 spaces, leave the last 2 spaces blank f02XrcB8hs0",
                ""
            )
            val parts = otpFormated.trim { it <= ' ' }.toCharArray()
            binding.edit1.setText(parts[0].toString())
            binding.edit2.setText(parts[1].toString())
            binding.edit3.setText(parts[2].toString())
            binding.edit4.setText(parts[3].toString())
            Log.e("otpFormated", "" + otpFormated)
            val code1: String = binding.edit1.text.toString().trim()
            val code2: String = binding.edit2.text.toString().trim()
            val code3: String = binding.edit3.text.toString().trim()
            val code4: String = binding.edit4.text.toString().trim()
            val code = code1 + code2 + code3 + code4
            processOtp(code)
        } else {
            val otpMessage = otp.replace("[#] Feenix: Your mobile verification OTP is ", "")
            val otpFormated = otpMessage.replace(
                ". If your app has 6 spaces, leave the last 2 spaces blank f02XrcB8hs0",
                ""
            )
            val parts = otpFormated.trim { it <= ' ' }.toCharArray()
            binding.edit1.setText(parts[0].toString())
            binding.edit2.setText(parts[1].toString())
            binding.edit3.setText(parts[2].toString())
            binding.edit4.setText(parts[3].toString())
            Log.e("otpFormated", "" + otpFormated)
            val code1: String = binding.edit1.text.toString().trim ()
            val code2: String = binding.edit2.text.toString().trim ()
            val code3: String = binding.edit3.text.toString().trim ()
            val code4: String = binding.edit4.text.toString().trim ()
            val code = code1 + code2 + code3 + code4
            processOtp(code)
        }
    }

    override fun onOtpTimeout() {
        ToastBuilder.build(mContext!!,"Time out, please resend")
    }

    var resOtpCode:String?=null
    override fun onSignInVerifyOtpResponse(signInVerifyOTPResponse: SignInVerifyOTPResponse,type:String) {
        resOtpCode= signInVerifyOTPResponse.otp
        if(type.equals("SendOtp"))
        {
            ToastBuilder.build(mContext!!,"Verification Code Sent Successfully")
        }
        else
        {
            ToastBuilder.build(mContext!!,"Resend OTP Successfully")
        }

        startSMSRetrieverClient()
    }

    override fun onSignInUpdateMobileNumber(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if(updateProfileMobileResponse.success)
        {
            authService?.VerifyOtp(this,"1")
        }
    }

    override fun onSignInVerifyOtp(success: String) {
        if (new == 1) {
            val bundle = Bundle()
            bundle.putString("Mobilenumber", phoneNumber)
            bundle.putString("MobileCountryCode", CountryCode)
            val intent = Intent(this@SignInVerifyPhoneActivity, SignInNameActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        } else if (new == 0) {
            if (first_name!!.isEmpty() && last_name!!.isEmpty()) {
                val bundle = Bundle()
                bundle.putString("Mobilenumber", phoneNumber)
                bundle.putString("MobileCountryCode", CountryCode)
                val intent = Intent(this@SignInVerifyPhoneActivity, SignInNameActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                authService?.getProfileDetails(this)
            }
        } else {
            authService?.getProfileDetails(this)
        }

    }


    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(this)
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid ->
            val task = SmsRetriever.getClient(mContext!!).startSmsUserConsent(phoneNumber)

        }
        task.addOnFailureListener { e ->
            ToastBuilder.build(mContext!!,"Error")
        }
    }

//Key Listeners

    private fun intiEditKeyListener() {
        binding.edit1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    binding.edit1.clearFocus()
                    binding.edit2.requestFocus()
                }
            }
        })
        binding.edit2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    binding.edit2.clearFocus()
                    binding.edit3.requestFocus()
                }
            }
        })
        binding.edit3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    binding.edit3.clearFocus()
                    binding.edit4.requestFocus()
                }
            }
        })
        binding.edit4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 0) {
                    binding.signInVerifyotp.setImageResource(R.drawable.ic_login_next_selected)
                } else {
                    binding.signInVerifyotp.setImageResource(R.drawable.ic_login_next_unselected)
                }
            }
        })
    }


    //get profile details
    override fun onGetProfileResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
            if(updateProfileMobileResponse.success)
            {
                myPreference?.dynamicMapkey= updateProfileMobileResponse.data?.android_user_mapkey
                myPreference?.email= updateProfileMobileResponse.data?.email
                myPreference?.firstName= updateProfileMobileResponse.data?.first_name
                myPreference?.lastName= updateProfileMobileResponse.data?.last_name
                myPreference?.mobile= updateProfileMobileResponse.data?.mobile
                myPreference?.countryCode= updateProfileMobileResponse.data?.country_code
                myPreference?.ReferralCode= updateProfileMobileResponse.data?.referal
                myPreference?.id= updateProfileMobileResponse.data?.id!!
                myPreference?.welcomeImage= updateProfileMobileResponse.data.welcome_image
                myPreference?.sosNumber= updateProfileMobileResponse.data.sos_number
                myPreference?.profilePic= updateProfileMobileResponse.data.picture
                myPreference?.fleet= updateProfileMobileResponse.data.fleet
                myPreference?.walletBal= updateProfileMobileResponse.data.wallet_balance
                moveHomeActivity(
                    updateProfileMobileResponse.data.active_request_flow,
                    updateProfileMobileResponse.data.active_request_id
                )

            }
            else
            {
                ToastBuilder.build(mContext!!,"Response Error")
            }

    }

    private fun moveHomeActivity(activeRequestFlow: String, activeRequestId: String) {


    }

}
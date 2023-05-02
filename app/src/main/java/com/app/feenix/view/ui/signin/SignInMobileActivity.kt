package com.app.feenix.view.ui.signin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.view.View
import android.widget.EditText
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.RequestModel.ResponseModel.SignInMobileResponse
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySignInMobileBinding
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.ISignInMobile
import com.app.feenix.webservices.SignIn.SignInService
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.hbb20.CountryCodePicker
import com.hellotirupathur.utils.TextChangedListener
import com.hellotirupathur.utils.Validator

class SignInMobileActivity : BaseActivity(), View.OnClickListener, ISignInMobile {
    private lateinit var binding: ActivitySignInMobileBinding
    var mContext: Context? = null
    private val CREDENTIAL_PICKER_REQUEST = 1
    private var authService: SignInService? = null
    private var myPreference: MyPreference? = null
    private lateinit var MobileNumber: EditText
    private lateinit var mobileCodePicker: CountryCodePicker
    private var mobileCountryCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        initCallbacks()
        requestHint()
    }

    private fun initCallbacks() {

        binding.signIn.setOnClickListener(this)
    }

    private fun initObject() {
        mContext = this@SignInMobileActivity
        myPreference = MyPreference(mContext!!)
        authService = SignInService()
        authService!!.SignInService(this@SignInMobileActivity)
        MobileNumber = binding.editMobile
        mobileCodePicker = binding.countryCodePicker
        TextChangedListener.onTextChanged(MobileNumber,binding.signIn)

    }

    // Get Mobile number from phone
    @SuppressLint("SuspiciousIndentation")
    @Suppress("DEPRECATION")
    @Throws(SendIntentException::class)
    private fun requestHint() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        startIntentSenderForResult(intent.intentSender, CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CREDENTIAL_PICKER_REQUEST ->                 // Obtain the phone number from the result
                if (resultCode == RESULT_OK) {
                    val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    // credential.getId();  <-- will need to process phone number string
                    if (credential!!.id.length == 13) {
                        val MobileNO = credential.id.substring(3)
                        MobileNumber.setText(MobileNO)
                    } else {
                        val MobileNO = credential.id.substring(4)
                        MobileNumber.setText(MobileNO)
                    }
                }
        }
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.signIn -> {
                mobileCountryCode = mobileCodePicker.selectedCountryCodeWithPlus

                if (Validator.isValidationEditext(MobileNumber, "Enter Mobile Number")) {
                    if(hasInternetConnection())
                    {
                        authService?.getLoginMobile(
                            this, MobileNumber.text.toString().trim(),
                            mobileCountryCode!!,""
                        )
                    }
                    else
                    {
                        ToastBuilder.build(mContext,"Please Connect internet and Try again")
                    }

                }
            }
        }
    }
    override fun onSignInMobileResponse(signInMobileResponse: SignInMobileResponse) {
        if (signInMobileResponse.success!!) {
            myPreference!!.token = signInMobileResponse.data?.access_token
            myPreference!!.Username =
                signInMobileResponse.data?.first_name + " " + signInMobileResponse.data?.last_name
            myPreference!!.mobile = mobileCountryCode + binding.editMobile.text.toString().trim()
            myPreference?.countryCode = mobileCountryCode
            val bundle = Bundle()
            bundle.putString("phoneNumber", binding.editMobile.text.toString().trim())
            bundle.putString("CountryCode", mobileCountryCode)
            bundle.putInt("new", signInMobileResponse.data?.new!!.toInt())
            bundle.putString("first_name", signInMobileResponse.data.first_name)
            bundle.putString("last_name", signInMobileResponse.data.last_name)
            MyActivity.launchWithBundle(mContext!!, SignInVerifyPhoneActivity::class.java, bundle)
        } else {
            val bundle = Bundle()
            bundle.putString("phoneNumber", binding.editMobile.text.toString().trim())
            bundle.putString("CountryCode", mobileCountryCode)
            MyActivity.launchWithBundle(mContext!!, SignInMultipleAccountsActivity::class.java, bundle)
        }
    }

}
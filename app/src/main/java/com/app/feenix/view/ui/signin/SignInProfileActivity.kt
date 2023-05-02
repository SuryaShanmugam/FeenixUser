package com.app.feenix.view.ui.signin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySignInProfileBinding
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.utils.Log
import com.app.feenix.utils.PermissionHandler
import com.app.feenix.view.ui.HomeActivity
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.SignIn.SignInService
import com.bumptech.glide.Glide
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource
import java.io.File

class SignInProfileActivity : BaseActivity(), View.OnClickListener, IGetProfileData,
    IUpdateProfile {
    private lateinit var binding: ActivitySignInProfileBinding
    var mContext: Context? = null
    private var authService: SignInService? = null

    private var myPreference: MyPreference? = null

    private val REQUEST_STORAGE_PERMISSION = 6
    private val REQUEST_FEED_IMAGE = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        initCallbacks()
    }

    private fun initCallbacks() {
        binding.signInProfile.setOnClickListener(this)
        binding.txtSkipProfile.setOnClickListener(this)
        binding.imgBackProfile.setOnClickListener(this)
        binding.editIcon.setOnClickListener(this)
    }

    private fun initObject() {
        mContext = this@SignInProfileActivity
        myPreference = MyPreference(mContext!!)
        authService = SignInService()
        authService!!.SignInService(this@SignInProfileActivity)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.editIcon -> {

                checkHasReadPermission()

            }
            R.id.signIn_profile -> {
                if (file != null) {
                    authService?.UpdateUserPic(this, file)
                } else {
                    Toast.makeText(mContext, "Please  add a profile picture", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            R.id.txt_skip_profile -> {
                authService?.getProfileDetails(this)
            }
            R.id.img_back_profile -> {
                onBackPressed()
            }
        }

    }

    private fun pickImage() {
        EasyImage.configuration(mContext!!).setImagesFolderName(getString(R.string.app_name))
        EasyImage.openChooserWithGallery(
            this,
            "Select Image",
            REQUEST_FEED_IMAGE
        )
    }

    var ImageUploadPackagePath = ""
    lateinit var file: File
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagesPicked(
                    imageFiles: List<File>,
                    source: ImageSource,
                    type: Int
                ) {
                    ImageUploadPackagePath = imageFiles[0].path
                    binding.usernameTextProfile.visibility = View.GONE
                    Glide.with(mContext!!).load(ImageUploadPackagePath).into(binding.imgProfilePic)
                    binding.signInProfile.setImageResource(R.drawable.ic_login_next_selected)
                    file = File(ImageUploadPackagePath)
                }
            })
    }

    private fun requestStoragePermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            arrayOf(Manifest.permission.CAMERA)

        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            arrayOf(Manifest.permission.CAMERA)
        }


    private fun checkHasReadPermission() {
        PermissionHandler.checkPermission(
            this,
            requestStoragePermission(),
            REQUEST_STORAGE_PERMISSION,
            object : PermissionHandler.PermissionHandleListener {
                override fun onPermissionGranted(requestCode: Int) {

                    pickImage()
                }

                override fun onPermissionDenied(requestCode: Int) {
                    checkHasReadPermission()
                }

                override fun onShowPermissionSettingsDialog(failedPermissions: Array<String?>) {

                }

                override fun onPermissionDontAllow() {
                    checkHasReadPermission()
                }

                override fun onPermissionAllow() {
                }
            })
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

    override fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {

        myPreference?.profilePic = updateProfileMobileResponse.data?.picture
        Log.d("fsreret", "" + updateProfileMobileResponse.data?.picture)
        MyActivity.launchClearStack(mContext!!, HomeActivity::class.java)


    }
}
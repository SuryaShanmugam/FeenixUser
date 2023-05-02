package com.app.feenix.view.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityProfileBinding
import com.app.feenix.model.request.UpdateProfileRequest
import com.app.feenix.model.response.UpdateProfileMobileResponse
import com.app.feenix.utils.PermissionHandler
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IGetProfileData
import com.app.feenix.viewmodel.IUpdateProfile
import com.app.feenix.webservices.SignIn.ProfileService
import com.bumptech.glide.Glide
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class ProfileActivity : BaseActivity(), View.OnClickListener, IUpdateProfile,IGetProfileData {


    private lateinit var binding: ActivityProfileBinding
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private val REQUEST_STORAGE_PERMISSION = 6
    private val REQUEST_FEED_IMAGE = 4
    private var profileservice: ProfileService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallback()
        initSetData()
    }

    private fun initCallback() {

        binding.imgBackupdateProfile.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
        binding.imageUpdateProfilepic.setOnClickListener(this)
    }

    private fun initSetData() {
        binding.editFirstname.setText(myPreference?.firstName)
        binding.editLastname.setText(myPreference?.lastName)
        binding.editEmail.setText(myPreference?.email)
        binding.mobileNumber.setText(myPreference?.mobile)
        binding.countryCodePicker.setFlagSize(0)
        binding.countryCodePicker.setCountryForPhoneCode(myPreference?.countryCode!!.toInt())

        if(myPreference?.profilePic!=null)
        {
            Glide.with(mContext!!).load(myPreference?.profilePic).into(binding.profilePicUpdate)
            binding.usernameTextProfile.visibility= View.GONE
        }
        else
        {
            Glide.with(mContext!!).load(R.drawable.bg_circle_profile_default).into(binding.profilePicUpdate)
            binding.usernameTextProfile.visibility= View.VISIBLE

        }
    }

    private fun initObjects() {
        mContext = this@ProfileActivity
        myPreference = MyPreference(mContext!!)
        profileservice = ProfileService()
        profileservice!!.ProfileService(this@ProfileActivity)
    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_backupdate_profile -> {
                onBackPressed()
            }
            R.id.btn_update -> {
                if (file != null) {
                    if(hasInternetConnection())
                    {
                        profileservice?.UpdateUserwithPic(this, file!!, UpdateProfileRequest(
                            binding.editFirstname.text.toString(),
                            binding.editLastname.text.toString(),
                            binding.mobileNumber.text.toString(),
                            binding.countryCodePicker.selectedCountryCodeWithPlus,
                            binding.editEmail.text.toString()))
                    }else
                    {
                        ToastBuilder.build(mContext!!,"No Internet, Please try again")
                    }

                } else {
                    if(hasInternetConnection())
                    {
                        profileservice?.UpdateUserwithoutPic(this, UpdateProfileRequest(
                            binding.editFirstname.text.toString(),
                            binding.editLastname.text.toString(),
                            binding.mobileNumber.text.toString(),
                            binding.countryCodePicker.selectedCountryCodeWithPlus,
                            binding.editEmail.text.toString()))
                    }
                    else{
                        ToastBuilder.build(mContext!!,"No Internet, Please try again")
                    }

                }

            }
            R.id.image_update_profilepic -> {
                checkHasReadPermission()
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
    var file: File? = null
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
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    ImageUploadPackagePath = imageFiles[0].path
                    binding.usernameTextProfile.visibility = View.GONE
                    Glide.with(mContext!!).load(ImageUploadPackagePath)
                        .into(binding.profilePicUpdate)
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

    override fun onGetProfileNameResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {
        if(updateProfileMobileResponse.success)
        {
            ToastBuilder.build(mContext!!,"Profile Updated Successfully")
            profileservice?.getProfileDetails(this)
        }
        else
        {
            ToastBuilder.build(mContext!!,"Profile Update Failed")

        }

    }

    override fun onGetProfileResponse(updateProfileMobileResponse: UpdateProfileMobileResponse) {

        if (updateProfileMobileResponse.success) {
            myPreference?.email = updateProfileMobileResponse.data?.email
            myPreference?.firstName = updateProfileMobileResponse.data?.first_name
            myPreference?.lastName = updateProfileMobileResponse.data?.last_name
            myPreference?.mobile = updateProfileMobileResponse.data?.mobile
            myPreference?.countryCode = updateProfileMobileResponse.data?.country_code
            myPreference?.profilePic = updateProfileMobileResponse.data?.picture

            binding.editFirstname.setText(updateProfileMobileResponse.data?.first_name)
            binding.editLastname.setText(updateProfileMobileResponse.data?.last_name)
            binding.editEmail.setText(updateProfileMobileResponse.data?.email)
            binding.mobileNumber.setText(updateProfileMobileResponse.data?.mobile)
        }
    }

}
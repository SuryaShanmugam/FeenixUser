package com.app.feenix.view.ui

import android.Manifest.permission
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySosAlertBinding
import com.app.feenix.model.response.SosContactResponse
import com.app.feenix.model.response.SosSendAlertResponse
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.ISos
import com.app.feenix.webservices.Sos.SosService

class SosAlertActivity : BaseActivity(), View.OnClickListener, ISos {
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private lateinit var binding: ActivitySosAlertBinding
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 103
    var countDownTimer: CountDownTimer? = null
    private var ContactlistSize = 0
    private var SosService: SosService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        processBundle()
        initObject()
        initCallbacks()
        initCountDownTimer()
    }

    private fun processBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            ContactlistSize = bundle.getInt("Contactlist")
        }
    }

    private fun initCallbacks() {

        binding.layoutSosAfterAlert.layoutCallPolice.setOnClickListener(this)
        binding.layoutSosInitialAlert.layoutCancelAlert.setOnClickListener(this)
    }

    private fun initObject() {
        mContext = this@SosAlertActivity
        myPreference = MyPreference(mContext!!)
        SosService = SosService()
        SosService?.SosService(mContext!!)
        if (ContactlistSize == 0) {
            binding.layoutSosAfterAlert.txtMessage.text = "Feenix safety team will contact you immediately, Add emergency contacts to alert them too"
        } else {
            binding.layoutSosAfterAlert.txtMessage.text = "Feenix safety team will contact you immediately, meanwhile we have aleted your emergency contacts"
        }


    }

    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.layoutSosInitialAlert.txtCount.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                callalert()
            }
        }.start()
    }

    private fun callalert() {
        if (hasInternetConnection()) {
            SosService?.CreateSendSos(this)
        } else {
            ToastBuilder.build(mContext!!, "No Internet, Please connect the internet")
        }
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.layout_cancel_alert -> {
                countDownTimer!!.cancel()
                onBackPressed()
                ToastBuilder.build(mContext, "Alert Cancelled")
            }
            R.id.layout_call_police -> {
                if (checkCallRequestPermissions()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + myPreference?.sosNumber)
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    startActivity(intent)
                }
            }
        }


    }

    override fun ongetEmergencyContactResponse(sosContactResponse: SosContactResponse) {


    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer!!.cancel()
    }

    override fun onCreateSendSosResponse(sosSendAlertResponse: SosSendAlertResponse) {
        if (sosSendAlertResponse.success) {
            binding.layoutSosAfterAlert.rootSosAfterAlert.visibility = View.VISIBLE
            binding.layoutSosInitialAlert.rootInitialSosAlert.visibility = View.GONE
        } else {
            binding.layoutSosAfterAlert.rootSosAfterAlert.visibility = View.GONE
            binding.layoutSosInitialAlert.rootInitialSosAlert.visibility = View.VISIBLE
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms[permission.CALL_PHONE] = PackageManager.PERMISSION_GRANTED
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    if (perms[permission.CALL_PHONE] == PackageManager.PERMISSION_GRANTED) {
                        if (checkCallRequestPermissions()) {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:" + myPreference?.sosNumber)
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission.CALL_PHONE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            startActivity(intent)
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission.CALL_PHONE
                            )
                        ) {
                            showDialogOK("Call Permission required",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkCallRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE -> {}
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .create()
            .show()
    }

    private fun checkCallRequestPermissions(): Boolean {
        val CallPhone = ContextCompat.checkSelfPermission(applicationContext, permission.CALL_PHONE)
        val listPermissionsNeeded = ArrayList<String>()
        if (CallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permission.CALL_PHONE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

}
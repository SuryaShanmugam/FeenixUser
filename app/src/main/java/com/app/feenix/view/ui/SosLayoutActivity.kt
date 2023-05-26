package com.app.feenix.view.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import cbs.com.bmr.Utilities.MyActivity.launch
import cbs.com.bmr.Utilities.MyActivity.launchWithBundle
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySosLayoutBinding
import com.app.feenix.model.response.SosContactData
import com.app.feenix.model.response.SosContactResponse
import com.app.feenix.model.response.SosSendAlertResponse
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.ISos
import com.app.feenix.webservices.Sos.SosService

class SosLayoutActivity : BaseActivity(), View.OnClickListener ,ISos{

    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private lateinit var binding: ActivitySosLayoutBinding
    private  var SosService:SosService?=null
    private val ContactList: ArrayList<SosContactData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        initCallbacks()
    }

    private fun initCallbacks() {
        binding.imgBackSos.setOnClickListener(this)
        binding.layoutAfterInclude.layoutAfterSosHelp.setOnClickListener(this)
        binding.layoutInitialInclude.layoutAddContact.setOnClickListener(this)
        binding.layoutAfterInclude.layoutCloseSos.setOnClickListener(this)
        binding.layoutAfterInclude.layoutAfterSosMessgae.setOnClickListener(this)

    }

    private fun initObject() {
        mContext = this@SosLayoutActivity
        myPreference=MyPreference(mContext!!)
        SosService= SosService()
        SosService?.SosService(mContext!!)
        if(hasInternetConnection())
        {
            SosService?.getEmergencyContacts(this)
        }
        else
        {
            ToastBuilder.build(mContext!!,"No Internet, Please connect the internet")
        }

    }

    override fun onClick(p0: View?) {
        val id= p0?.id
        when(id)
        {
            R.id.img_back_sos->{
                onBackPressed()
            }
            R.id.initial_get_help->
            {

                if (ContactList != null) {
                    val bundle = Bundle()
                    bundle.putInt("Contactlist", ContactList.size)
                    launchWithBundle(mContext!!, SosAlertActivity::class.java, bundle)
                } else {
                    val bundle = Bundle()
                    bundle.putInt("Contactlist", 0)
                    launchWithBundle(mContext!!, SosAlertActivity::class.java, bundle)
                }

            }
            R.id.layout_add_contact->
            {
                launch(mContext!!, SosEmergencyContactActivity::class.java)
            }
            R.id.layout_after_sos_messgae->
            {
                if(hasInternetConnection())
                {
                    SosService?.CreateSendSos(this)
                }
                else
                {
                    ToastBuilder.build(mContext!!,"No Internet, Please connect the internet")
                }
            }
            R.id.layout_after_sos_help->
            {
                val bundle = Bundle()
                bundle.putInt("Contactlist", ContactList.size)
                launchWithBundle(mContext!!, SosAlertActivity::class.java, bundle)
            }
            R.id.layout_close_sos->
            {
                onBackPressed()
            }
        }
    }

    private fun CreateAlertHelp() {


    }

    override fun ongetEmergencyContactResponse(sosContactResponse: SosContactResponse) {
        ContactList.clear()
        if(sosContactResponse!=null)
        {
            ContactList.addAll(sosContactResponse.contacts)
            if (sosContactResponse.contacts.size == 5 || sosContactResponse.contacts.size > 5) {
                binding.layoutAfterInclude.rootAfterSos.visibility=View.VISIBLE
                binding.layoutInitialInclude.rootInitialLayout.visibility=View.GONE
            } else {
                binding.layoutAfterInclude.rootAfterSos.visibility=View.GONE
                binding.layoutInitialInclude.rootInitialLayout.visibility=View.VISIBLE
            }

        }

    }

    override fun onCreateSendSosResponse(sosSendAlertResponse: SosSendAlertResponse) {

        if (sosSendAlertResponse.success) {
            ToastBuilder.build(mContext, "Ride details shared successfully")
            binding.layoutAfterInclude.imgAfterSmsSend.setImageResource(R.drawable.ic_checked_right)
            binding.layoutAfterInclude.txtMessageSent.setTextColor(ContextCompat.getColor(mContext!!, R.color.acceptGreen))
            binding.layoutAfterInclude.txtMessageSent.text = "Alert Sent Successfully"
        } else {
            ToastBuilder.build(mContext, sosSendAlertResponse.message)
        }
    }
}
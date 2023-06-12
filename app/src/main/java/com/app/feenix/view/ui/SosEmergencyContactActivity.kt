package com.app.feenix.view.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySosEmergencyContactBinding
import com.app.feenix.view.ui.base.BaseActivity

class SosEmergencyContactActivity : BaseActivity(), View.OnClickListener {
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private lateinit var binding: ActivitySosEmergencyContactBinding

    private var mLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosEmergencyContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallbacks()
        initRecyclerView()
        getContactList()
    }

    private fun initCallbacks() {

        binding.imgBackContact.setOnClickListener(this)
        binding.btnShareDetails.setOnClickListener(this)
        binding.layoutAddContact.setOnClickListener(this)
    }

    private fun initObjects() {
        mContext = this
        mLayoutManager = LinearLayoutManager(mContext)

    }

    private fun initRecyclerView() {
        binding.recyclerviewNotifications.layoutManager = mLayoutManager
        binding.recyclerviewNotifications.isNestedScrollingEnabled = false
        binding.recyclerviewNotifications.setHasFixedSize(true)
        binding.recyclerviewNotifications.adapter = mAdapter
    }

    override fun onClick(p0: View?) {
        val id= p0?.id
        when(id)
        {
            R.id.img_back_contact->
            {
                onBackPressed()
            }
            R.id.btn_ShareDetails->
            {

            }
            R.id.layout_add_contact->
        {

        }

        }

    }
}
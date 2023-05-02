package com.app.feenix.view.ui.referAndearn

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.ResponseModel.ReferalResponse
import com.app.feenix.databinding.FragmentEarningsBinding
import com.app.feenix.view.ui.base.BaseFragment
import com.app.feenix.viewmodel.IReferalData
import com.app.feenix.webservices.promotionsAndReferals.ReferalService

class EarningsFragment : BaseFragment(), IReferalData {

    private var mContext: Context? = null
    private lateinit var binding: FragmentEarningsBinding
    private lateinit var referalService: ReferalService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarningsBinding.inflate(inflater, container, false)
        initObjects()
        return binding.root
    }


    private fun initObjects() {
        mContext = activity
        referalService = ReferalService()
        referalService.ReferalService(mContext!!)
        if (hasInternetConnection()) {
            referalService.getReferals(this)
        } else {
            ToastBuilder.build(mContext, "Please connect internet and try again")
        }
    }

    override fun ongetReferalResponse(referalResponse: ReferalResponse) {
        if (referalResponse.success) {
            binding.txtUsercount.text = referalResponse.user_referals.toString()
            binding.txtDrivercount.text = referalResponse.driver_referals.toString()
        } else {
            ToastBuilder.build(mContext, "Unable to Fetch Data")
        }


    }


}
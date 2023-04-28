package com.app.feenix.view.activities.promotions

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.ResponseModel.PromotionResponse
import com.app.biu.model.ResponseModel.PromotionsData
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityPromotionsBinding
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.viewmodel.IPromotionData
import com.app.feenix.webservices.promotions.PromotionService

class PromotionsActivity : BaseActivity(), View.OnClickListener, IPromotionData {


    private lateinit var binding: ActivityPromotionsBinding
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private var promotionService: PromotionService? = null
    var Promocodes = ""
    private lateinit var mPaymentTransactionList: ArrayList<PromotionsData>

    //var mAdapter: WalletTransactionAdapter? = null
    var show = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallback()
        initRecyclerview()
    }

    private fun initRecyclerview() {

        binding.rvPromotions.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        //  binding.rvPromotions.adapter = mAdapter
        binding.rvPromotions.isNestedScrollingEnabled
    }

    private fun initCallback() {

        binding.imgPromotionBack.setOnClickListener(this)
        binding.redeemCoupon.setOnClickListener(this)
        binding.txtShowAvailablePromocodes.setOnClickListener(this)
    }


    private fun initObjects() {
        mContext = this@PromotionsActivity
        myPreference = MyPreference(mContext!!)
        promotionService = PromotionService()
        promotionService?.PromotionService(mContext!!)
        mPaymentTransactionList = ArrayList()

        if (hasInternetConnection()) {
            promotionService?.getPromocodes(this)
        } else {
            ToastBuilder.build(mContext, "Please connect internet and try again")
        }
    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_promotion_back -> {
                onBackPressed()
            }
            R.id.txt_show_available_promocodes -> {
                if (show == 0) {
                    binding.rvPromotions.visibility = View.VISIBLE
                    show++
                } else {
                    binding.rvPromotions.visibility = View.GONE
                    binding.redeemCoupon.visibility = View.GONE
                    Promocodes = ""
                    show--
                }
            }
        }
    }

    override fun ongetPromotionCodes(promotionResponse: PromotionResponse) {

    }


}
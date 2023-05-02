package com.app.feenix.view.ui.promotions

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.ResponseModel.AddPromocodeResponse
import com.app.biu.model.ResponseModel.PromotionResponse
import com.app.biu.model.ResponseModel.PromotionsData
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityPromotionsBinding
import com.app.feenix.model.request.AddPromocode
import com.app.feenix.view.adapter.PromotionsAdapter
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IPromotionData
import com.app.feenix.webservices.promotionsAndReferals.PromotionService
import com.hellotirupathur.utils.TextChangedListener

class PromotionsActivity : BaseActivity(), View.OnClickListener, IPromotionData,
    PromotionsAdapter.PromotionCallback {


    private lateinit var binding: ActivityPromotionsBinding
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private var promotionService: PromotionService? = null
    var Promocodes = ""
    private lateinit var mPromotionsList: ArrayList<PromotionsData>

    var mAdapter: PromotionsAdapter? = null
    var show = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        processBundle()
        initObjects()
        initCallback()
        initRecyclerview()
    }

    private var Type: String? = null
    private fun processBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            Type = bundle.getString("Type")
        }


    }

    private fun initRecyclerview() {

        binding.rvPromotions.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvPromotions.adapter = mAdapter
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

        mPromotionsList = java.util.ArrayList()
        mAdapter = PromotionsAdapter(mContext!!, mPromotionsList, this)
        if (hasInternetConnection()) {
            promotionService?.getPromocodes(this)
        } else {
            ToastBuilder.build(mContext, "Please connect internet and try again")
        }
        if (Type == "2") {
            binding.txtAvaiablecoupons.visibility = View.VISIBLE
            binding.layoutEntercodes.visibility = View.GONE
            binding.rvPromotions.visibility = View.VISIBLE
        } else {
            binding.txtAvaiablecoupons.visibility = View.GONE
            binding.rvPromotions.visibility = View.GONE
            binding.layoutEntercodes.visibility = View.VISIBLE
        }
        TextChangedListener.onTextPromocodesChanged(
            binding.editPromocodes, mContext!!,
            binding.redeemCoupon, binding.txtShowError
        )

    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_promotion_back -> {
                0
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
            R.id.redeemCoupon -> {
                var refferal: Int

                if (Type == "2") {
                    refferal = 0
                } else {
                    refferal = 1
                }
                if (Promocodes.isNotEmpty()) {
                    if (hasInternetConnection()) {
                        promotionService?.AddPromocodes(
                            this,
                            AddPromocode(Promocodes, refferal)
                        )
                    } else {
                        ToastBuilder.build(mContext, "Please connect internet and try again")
                    }

                } else {
                    if (hasInternetConnection()) {
                        promotionService?.AddPromocodes(
                            this,
                            AddPromocode(binding.editPromocodes.text.toString(), refferal)
                        )
                    } else {
                        ToastBuilder.build(mContext, "Please connect internet and try again")
                    }


                }

            }
        }
    }

    override fun ongetPromotionCodes(promotionResponse: PromotionResponse) {
        mPromotionsList.clear()
        mPromotionsList.addAll(promotionResponse.available_promo)
        mAdapter?.notifyDataSetChanged()
    }

    override fun onAddPromotionCodeRes(addPromocodeResponse: AddPromocodeResponse) {
        if (addPromocodeResponse.success) {
            if (Type == "2") {
                ToastBuilder.build(
                    this@PromotionsActivity,
                    resources.getString(R.string.promo_added_successfully)
                )
            } else {
                binding.editPromocodes.setText("")
                ToastBuilder.build(
                    this@PromotionsActivity,
                    resources.getString(R.string.promo_added_successfully)
                )
                if (hasInternetConnection()) {
                    promotionService?.getPromocodes(this)
                } else {
                    ToastBuilder.build(mContext, "Please connect internet and try again")
                }
            }
        } else {
            binding.txtShowError.text = addPromocodeResponse.message
            binding.txtShowError.visibility = View.VISIBLE
            binding.editPromocodes.setBackgroundResource(R.drawable.bg_editext_error_rectangle)
            binding.editPromocodes.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.acceptRed
                )
            )
        }

    }

    override fun onItemClick(position: Int) {
        val promotions: PromotionsData = mPromotionsList[position]
        Promocodes = promotions.promo_code!!
        binding.redeemCoupon.visibility = View.VISIBLE
    }


}
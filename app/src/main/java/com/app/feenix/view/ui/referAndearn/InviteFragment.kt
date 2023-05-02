package com.app.feenix.view.ui.referAndearn

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.biu.model.ResponseModel.AddPromocodeResponse
import com.app.biu.model.ResponseModel.PromotionResponse
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.FragmentInviteBinding
import com.app.feenix.model.request.AddPromocode
import com.app.feenix.view.ui.base.BaseFragment
import com.app.feenix.viewmodel.IPromotionData
import com.app.feenix.webservices.promotionsAndReferals.PromotionService
import java.util.*

class InviteFragment : BaseFragment(), View.OnClickListener, IPromotionData {

    private var mContext: Context? = null
    private lateinit var binding: FragmentInviteBinding
    private var urlToShare: String? = null
    private lateinit var myPreference: MyPreference
    private var promotionService: PromotionService? = null
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteBinding.inflate(inflater, container, false)
        initObjects()
        initCallbacks()
        return binding.root
    }

    private fun initCallbacks() {

        binding.fbShare.setOnClickListener(this)
        binding.whatsappShare.setOnClickListener(this)
        binding.layoutMore.setOnClickListener(this)
        binding.Instashare.setOnClickListener(this)
        binding.btnCopy.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }


    private fun initObjects() {
        mContext = activity
        myPreference = MyPreference(mContext!!)
        myClipboard = mContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        urlToShare = resources.getString(R.string.share_content) + myPreference.ReferralCode
        promotionService = PromotionService()
        promotionService?.PromotionService(mContext!!)
        binding.referralCode.text = myPreference.ReferralCode
    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.fbShare -> {
                var intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare)
                var facebookAppFound = false
                val matches = mContext!!.packageManager.queryIntentActivities(intent, 0)
                for (info in matches) {
                    if (info.activityInfo.packageName.lowercase(Locale.getDefault())
                            .startsWith("com.facebook.katana")
                    ) {
                        intent.setPackage(info.activityInfo.packageName)
                        facebookAppFound = true
                        break
                    }
                }
                if (!facebookAppFound) {
                    val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$urlToShare"
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
                }

                startActivity(intent)
            }

            R.id.whatsappShare -> {
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.setPackage("com.whatsapp")
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, urlToShare)
                try {
                    startActivity(whatsappIntent)
                } catch (ex: ActivityNotFoundException) {
                    ToastBuilder.build(activity, "Whatsapp have not been installed.")
                }
            }
            R.id.Instashare -> {
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.setPackage("com.instagram.android")
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, urlToShare)
                try {
                    startActivity(whatsappIntent)
                } catch (ex: ActivityNotFoundException) {
                    ToastBuilder.build(activity, "Instagram have not been installed.")
                }
            }
            R.id.layout_more -> {
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, urlToShare)
                try {
                    startActivity(whatsappIntent)
                } catch (ex: ActivityNotFoundException) {
                    ToastBuilder.build(activity, "Apps have not been installed.")
                }
            }

            R.id.btn_copy -> {
                val text: String = binding.referralCode.text.toString()
                myClip = ClipData.newPlainText("text", text)
                myClipboard?.setPrimaryClip(myClip!!)
                Toast.makeText(activity, "Text Copied", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_apply -> {
                if (binding.editReferalCode.text.toString().isEmpty()) {
                    binding.editReferalCode.requestFocus()
                    binding.editReferalCode.error = "Please Enter Referal Code"
                } else {
                    promotionService?.AddPromocodes(
                        this,
                        AddPromocode(binding.editReferalCode.text.toString(), 1)
                    )
                }
            }
        }
    }

    override fun ongetPromotionCodes(promotionResponse: PromotionResponse) {


    }

    override fun onAddPromotionCodeRes(addPromocodeResponse: AddPromocodeResponse) {
        if (addPromocodeResponse.success) {
            ToastBuilder.build(mContext!!, resources.getString(R.string.promo_added_successfully))
        } else {
            binding.txtShowError.text = addPromocodeResponse.message
            binding.txtShowError.visibility = View.VISIBLE
            binding.editReferalCode.setBackgroundResource(R.drawable.bg_editext_error_rectangle)
            binding.editReferalCode.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.acceptRed
                )
            )
        }
    }


}
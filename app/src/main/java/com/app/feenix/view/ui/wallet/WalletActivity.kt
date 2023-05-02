package com.app.feenix.view.ui.wallet

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityWalletBinding
import com.app.feenix.model.request.AddMoneyWallet
import com.app.feenix.model.response.AddMoneyWalletResponse
import com.app.feenix.model.response.WalletResponse
import com.app.feenix.model.response.WalletTransactionList
import com.app.feenix.view.adapter.WalletTransactionAdapter
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IWalletData
import com.app.feenix.webservices.wallet.WalletService
import com.hbb20.CountryCodePicker

class WalletActivity : BaseActivity(), View.OnClickListener, IWalletData {


    private lateinit var binding: ActivityWalletBinding
    var mContext: Context? = null
    private var myPreference: MyPreference? = null
    private var walletService: WalletService? = null
    private lateinit var mPaymentTransactionList: ArrayList<WalletTransactionList>
    var mAdapter: WalletTransactionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObjects()
        initCallback()
        initRecyclerview()
    }

    private fun initRecyclerview() {

        binding.rvTransaction.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvTransaction.adapter = mAdapter
        binding.rvTransaction.isNestedScrollingEnabled
    }

    private fun initCallback() {

        binding.imgWalletBack.setOnClickListener(this)
        binding.btnTopupwallet.setOnClickListener(this)
    }


    private fun initObjects() {
        mContext = this@WalletActivity
        myPreference = MyPreference(mContext!!)
        walletService = WalletService()
        walletService?.WalletService(mContext!!)
        mPaymentTransactionList = ArrayList()
        mAdapter = WalletTransactionAdapter(this, mPaymentTransactionList)
        if (hasInternetConnection()) {
            walletService?.getWalletBalance(this)
        } else {
            ToastBuilder.build(mContext, "Please connect internet and try again")
        }
    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_wallet_back -> {
                onBackPressed()
            }
            R.id.btn_topupwallet -> {
                dialogTopupWallet()
            }
        }
    }


    override fun onWalletResponse(walletResponse: WalletResponse) {
        binding.walletAmount.text =
            resources.getString(R.string.money_symbols) + walletResponse.wallet_balance
        if (walletResponse.transactions.size > 0) {
            binding.rvTransaction.visibility = View.VISIBLE
            binding.noDataAvailable.visibility = View.GONE
            mPaymentTransactionList.clear()
            mPaymentTransactionList.addAll(walletResponse.transactions)
            mAdapter?.notifyDataSetChanged()
        } else {
            binding.rvTransaction.visibility = View.GONE
            binding.noDataAvailable.visibility = View.VISIBLE
        }

    }


    // Topup Wallet
    var TopupDialog: Dialog? = null
    var SelectPaymentDialog: Dialog? = null
    var MobileMoneyModeDialog: Dialog? = null

    private fun dialogTopupWallet() {

        TopupDialog = Dialog(this@WalletActivity)
        TopupDialog!!.setCancelable(true)
        TopupDialog!!.setCanceledOnTouchOutside(true)
        TopupDialog!!.setContentView(R.layout.dialog_topup_wallet)
        TopupDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = TopupDialog!!.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        TopupDialog!!.show()

        val hundred_amount = TopupDialog!!.findViewById<Button>(R.id.hundred_amount)
        val two_hundred_amount = TopupDialog!!.findViewById<Button>(R.id.two_hundred_amount)
        val five_hundred_amount = TopupDialog!!.findViewById<Button>(R.id.five_hundred_amount)
        val four_hundred_amount = TopupDialog!!.findViewById<Button>(R.id.four_hundred_amount)
        val addMoney = TopupDialog!!.findViewById<Button>(R.id.addMoney)
        val walletInput = TopupDialog!!.findViewById<EditText>(R.id.walletInput)
        val mImagecloseDialog = TopupDialog!!.findViewById<ImageView>(R.id.img_close)
        mImagecloseDialog.setOnClickListener { TopupDialog!!.dismiss() }

        hundred_amount.setOnClickListener { walletInput.setText("10") }
        two_hundred_amount.setOnClickListener { walletInput.setText("20") }
        five_hundred_amount.setOnClickListener { walletInput.setText("30") }
        four_hundred_amount.setOnClickListener { walletInput.setText("40") }
        walletInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 0) {
                    addMoney.setBackgroundResource(R.drawable.bg_rect_primary)
                    addMoney.setTextColor(
                        ContextCompat.getColor(
                            this@WalletActivity,
                            R.color.black
                        )
                    )
                } else {
                    addMoney.setBackgroundResource(R.drawable.bg_home_btn)
                }
            }
        })
        addMoney.setOnClickListener {
            if (!walletInput.text.toString().isEmpty()) {
                TopupDialog!!.dismiss()
                dialogSelectPayment(walletInput.text.toString(), "")
            } else {
                ToastBuilder.build(mContext, "Please enter amount")
            }
        }
    }

    private var PaymentMode: String? = null
    private var NetworkType: String? = null
    private fun dialogSelectPayment(amount: String, payToken: String) {
        SelectPaymentDialog = Dialog(this@WalletActivity)
        SelectPaymentDialog!!.setCancelable(true)
        SelectPaymentDialog!!.setCanceledOnTouchOutside(true)
        SelectPaymentDialog?.setContentView(R.layout.dialog_select_payment)
        SelectPaymentDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = SelectPaymentDialog!!.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        SelectPaymentDialog!!.show()
        val mImagecloseDialog = SelectPaymentDialog!!.findViewById<ImageView>(R.id.img_close)
        val radioButton_momo =
            SelectPaymentDialog!!.findViewById<RadioButton>(R.id.radioButton_momo)
        val radioButton_card =
            SelectPaymentDialog!!.findViewById<RadioButton>(R.id.radioButton_card)
        val btn_confirm = SelectPaymentDialog!!.findViewById<Button>(R.id.btn_confirm)
        val layout_card_payment =
            SelectPaymentDialog!!.findViewById<LinearLayout>(R.id.layout_card_payment)
        val layout_mobile_money =
            SelectPaymentDialog!!.findViewById<LinearLayout>(R.id.layout_mobile_money)
        mImagecloseDialog.setOnClickListener { SelectPaymentDialog!!.dismiss() }
        btn_confirm.setOnClickListener {
            if (PaymentMode == "MobileMoney") {
                dialogPaymentMobileMoney(amount, payToken)
                SelectPaymentDialog!!.dismiss()
            } else {
                val cardUrl =
                    "https://app.slydepay.com/invoicing/slydepay/$payToken"
                val bundle = Bundle()
                bundle.putString("CardPaymentUrl", cardUrl)
                bundle.putString("PaymentType", "1")
                bundle.putString("amount", amount)
                bundle.putString("payToken", payToken)
                //  val intent = Intent(this@WalletActivity, PaymentGatewayWebview::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        layout_mobile_money.setOnClickListener {
            PaymentMode = "MobileMoney"
            radioButton_momo.isChecked = true
            radioButton_card.isChecked = false
        }
        layout_card_payment.setOnClickListener {
            PaymentMode = "Card"
            radioButton_momo.isChecked = false
            radioButton_card.isChecked = true
        }
        radioButton_momo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                PaymentMode = "MobileMoney"
                radioButton_momo.isChecked = true
                radioButton_card.isChecked = false
            }
        }
        radioButton_card.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                PaymentMode = "Card"
                radioButton_momo.isChecked = false
                radioButton_card.isChecked = true
            }
        }
    }

    private fun dialogPaymentMobileMoney(amount: String, payToken: String) {
        MobileMoneyModeDialog = Dialog(this@WalletActivity)
        MobileMoneyModeDialog!!.setCancelable(true)
        MobileMoneyModeDialog!!.setCanceledOnTouchOutside(true)
        MobileMoneyModeDialog?.setContentView(R.layout.dialog_select_paymentmode)
        MobileMoneyModeDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = MobileMoneyModeDialog!!.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        MobileMoneyModeDialog!!.show()
        val mImagecloseDialog = MobileMoneyModeDialog!!.findViewById<ImageView>(R.id.img_close)
        val layout_MTN = MobileMoneyModeDialog!!.findViewById<LinearLayout>(R.id.layout_MTN)
        val layout_Airtel = MobileMoneyModeDialog!!.findViewById<LinearLayout>(R.id.layout_Airtel)
        val layout_Vodafone =
            MobileMoneyModeDialog!!.findViewById<LinearLayout>(R.id.layout_Vodafone)
        val txt_mtn = MobileMoneyModeDialog!!.findViewById<TextView>(R.id.txt_mtn)
        val txt_airtel = MobileMoneyModeDialog!!.findViewById<TextView>(R.id.txt_airtel)
        val txt_vodafone = MobileMoneyModeDialog!!.findViewById<TextView>(R.id.txt_vodafone)
        val btn_confirm = MobileMoneyModeDialog!!.findViewById<Button>(R.id.btn_confirm)
        val layout_mobileno =
            MobileMoneyModeDialog!!.findViewById<LinearLayout>(R.id.layout_mobileno)
        val mobile_number = MobileMoneyModeDialog!!.findViewById<EditText>(R.id.mobile_number)
        val countryCodePicker =
            MobileMoneyModeDialog!!.findViewById<CountryCodePicker>(R.id.countryCodePicker)
        mImagecloseDialog.setOnClickListener { MobileMoneyModeDialog!!.dismiss() }
        layout_MTN.setOnClickListener {
            NetworkType = "MTN_MONEY"
            txt_mtn.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.primary
                )
            )
            txt_airtel.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            txt_vodafone.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            layout_mobileno.visibility = View.VISIBLE
        }
        layout_Airtel.setOnClickListener {
            NetworkType = "AIRTEL_MONEY"
            txt_mtn.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            txt_airtel.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.primary
                )
            )
            txt_vodafone.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            layout_mobileno.visibility = View.VISIBLE
        }
        layout_Vodafone.setOnClickListener {
            NetworkType = "VODAFONE_CASH_PROMPT"
            txt_mtn.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            txt_airtel.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.slide_text_color
                )
            )
            txt_vodafone.setTextColor(
                ContextCompat.getColor(
                    this@WalletActivity,
                    R.color.primary
                )
            )
            layout_mobileno.visibility = View.VISIBLE
        }
        btn_confirm.setOnClickListener {
            if (NetworkType!!.isEmpty()) {
                ToastBuilder.build(this@WalletActivity, "Please Select Network")
            } else if (mobile_number.text.toString().isEmpty()) {
                ToastBuilder.build(
                    this@WalletActivity,
                    "Please Enter Mobile Number"
                )
            } else {

                walletService?.AddMoney(
                    this, AddMoneyWallet(
                        amount,
                        NetworkType,
                        "MOBILE",
                        payToken,
                        mobile_number.text.toString()
                    )
                )

            }
        }
    }

    override fun onAddMoneyWalletResponse(addMoneyWalletResponse: AddMoneyWalletResponse) {
        if (addMoneyWalletResponse.success!!) {
            TopupDialog!!.dismiss()
            MobileMoneyModeDialog!!.dismiss()
            PaymentSuccess("Payment Processing...", addMoneyWalletResponse.message!!)
        } else {
            PaymentSuccess("Payment Failure!", addMoneyWalletResponse.message!!)
        }

    }


    private fun PaymentSuccess(Title: String, Message: String) {
        val dialogServiceBookedSuccess = Dialog(this@WalletActivity, R.style.RoundedCornersDialog)
        dialogServiceBookedSuccess.setCancelable(false)
        dialogServiceBookedSuccess.setCanceledOnTouchOutside(false)
        dialogServiceBookedSuccess.setContentView(R.layout.dialog_waiting_payment_wallet)
        val window = dialogServiceBookedSuccess.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)
        dialogServiceBookedSuccess.show()
        val btn_proceed = dialogServiceBookedSuccess.findViewById<Button>(R.id.btn_proceed)
        val txt_message = dialogServiceBookedSuccess.findViewById<TextView>(R.id.txt_message)
        val txt_title = dialogServiceBookedSuccess.findViewById<TextView>(R.id.txt_title)
        txt_title.text = Title
        txt_message.text = Message
        btn_proceed.setOnClickListener {
            walletService?.getWalletBalance(this)
            dialogServiceBookedSuccess.dismiss()
        }
    }


}
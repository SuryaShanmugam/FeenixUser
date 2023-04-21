package com.app.feenix.view.activities.tripdetails

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.biu.model.RequestModel.ResponseModel.RideTripResponseData
import com.app.feenix.R
import com.app.feenix.databinding.ActivityTripDetailBinding
import com.app.feenix.utils.Utils
import com.app.feenix.view.activities.base.BaseActivity
import com.bumptech.glide.Glide
import java.lang.String

class TripDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTripDetailBinding
    private lateinit var rideTripResponseData: RideTripResponseData
    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initProcessBundle()
        initObjects()
        initCallbacks()
    }

    private fun initProcessBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            rideTripResponseData = bundle.getParcelable("RideDetails")!!
        }

    }

    private fun initCallbacks() {
        binding.imgBackTripdetails.setOnClickListener(this)
        binding.viewReceiptButton.setOnClickListener(this)
    }

    private fun initObjects() {
        mContext = this@TripDetailActivity
        setData()

    }

    private fun setData() {
        if (rideTripResponseData.service_type?.is_delivery == 1) {
            binding.dateTime.text = Utils.tripsDateformat(
                "yyyy-MM-dd HH:mm:ss",
                "EEE, MMM dd , hh:mm aaa", rideTripResponseData.assigned_at
            )
            binding.fromAddress.text = rideTripResponseData.s_address
            binding.toAddress.text = rideTripResponseData.d_address
            if (rideTripResponseData.started_at != null) {
                binding.txtPickupTime.text = Utils.tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "hh:mm aaa",
                    rideTripResponseData.started_at
                )

            }
            if (rideTripResponseData.finished_at != null) {
                binding.txtDropTime.text = Utils.tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "hh:mm aaa",
                    rideTripResponseData.finished_at
                )

            }
            if (rideTripResponseData.status.equals("CANCELLED", ignoreCase = true)) {
                binding.viewReceiptButton.visibility = View.GONE
                binding.cancelledByLayout.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.GONE
                binding.layoutBilldetails.visibility = View.GONE
                if (rideTripResponseData.cancelled_reason === "null" || rideTripResponseData.cancelled_reason === "") {
                    binding.cancelledReason.text = "Reason: N / A"
                } else {
                    binding.cancelledReason.text =
                        "Reason: " + rideTripResponseData.cancelled_reason
                }
                binding.status.text = "Cancelled"
                binding.status.setTextColor(ContextCompat.getColor(this, R.color.trip_cancelled))
            } else {
                binding.viewReceiptButton.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.VISIBLE
                binding.layoutBilldetails.visibility = View.VISIBLE
                binding.status.text = "Completed"
                binding.status.setTextColor(ContextCompat.getColor(this, R.color.acceptGreen))
            }
            binding.deliveryDetailsButton.visibility = View.GONE

            if (rideTripResponseData.provider_profiles?.car_registration != null) {
                binding.carnumber.text = rideTripResponseData.provider_profiles?.car_registration
            } else {
                binding.carnumber.text = "N/A"
            }



            if (rideTripResponseData.payment != null && rideTripResponseData.payment?.trip_fare != null) {
                binding.Tripfare.text =
                    resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.trip_fare
//            lbl_tripfare.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    invoiceDetailDialog(paymentjsonObject.optString("fixed"), paymentjsonObject.optString("distance_price"),
//                            paymentjsonObject.optString("time_price"),
//                            paymentjsonObject.optString("distance"), paymentjsonObject.optString("time_taken")
//                    );
//
//                }
//            });
            }

            Glide.with(mContext!!).load(rideTripResponseData.provider?.avatar).placeholder(
                ContextCompat.getDrawable(mContext!!, R.drawable.taxi_placeholder)
            ).dontAnimate()
                .into(binding.carImage)
            binding.carName.text = rideTripResponseData.service_type?.name

            if (rideTripResponseData.service_type?.name.equals("Economy", ignoreCase = true)) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_cartype_economy)
            } else if (rideTripResponseData.service_type?.name.equals("Comfort", ignoreCase = true)
            ) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_cartype_comfort)
            } else if (rideTripResponseData.service_type?.name.equals("Premium", ignoreCase = true)
            ) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_cartype_premium)
            }
            binding.drivernameTrip.text = rideTripResponseData.provider?.first_name
            binding.fromAddress.text = rideTripResponseData.s_address
            binding.toAddress.text = rideTripResponseData.d_address
            binding.paymentType.text = rideTripResponseData.payment_mode

        } else {
            binding.paymentType.text = rideTripResponseData.payment_mode
            binding.fromAddress.text = rideTripResponseData.s_address
            binding.toAddress.text = rideTripResponseData.d_address
            binding.dateTime.text = Utils.tripsDateformat(
                "yyyy-MM-dd HH:mm:ss",
                "EEE, MMM dd , hh:mm aaa", rideTripResponseData.assigned_at
            )
            binding.fromAddress.text = rideTripResponseData.s_address
            binding.toAddress.text = rideTripResponseData.d_address
            if (rideTripResponseData.started_at != null) {
                binding.txtPickupTime.text = Utils.tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "hh:mm aaa",
                    rideTripResponseData.started_at
                )

            }
            if (rideTripResponseData.finished_at != null) {
                binding.txtDropTime.text = Utils.tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "hh:mm aaa",
                    rideTripResponseData.finished_at
                )

            }
            //Delivery Mode
            binding.deliveryDetailsButton.visibility = View.VISIBLE

            Glide.with(mContext!!).load(rideTripResponseData.provider?.avatar).placeholder(
                ContextCompat.getDrawable(mContext!!, R.drawable.taxi_placeholder)
            ).dontAnimate()
                .into(binding.carImage)
            binding.carName.text = rideTripResponseData.service_type?.name

            binding.drivernameTrip.text = rideTripResponseData.provider?.first_name

            if (rideTripResponseData.provider_profiles?.car_registration != null) {
                binding.carnumber.text = rideTripResponseData.provider_profiles?.car_registration
            } else {
                binding.carnumber.text = "N/A"
            }

            if (rideTripResponseData.service_type?.name.equals("MotorBike", ignoreCase = true)) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_service_type_bike)
            } else if (rideTripResponseData.service_type?.name.equals("Mini Van", ignoreCase = true)
            ) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_service_type_van)
            } else if (rideTripResponseData.service_type?.name.equals("Van", ignoreCase = true)
            ) {
                binding.servicetypeImg.setImageResource(R.drawable.ic_service_type_van)
            }

            if (rideTripResponseData.status.equals("CANCELLED", ignoreCase = true)) {
                binding.viewReceiptButton.visibility = View.GONE
                binding.cancelledByLayout.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.GONE
                binding.layoutBilldetails.visibility = View.GONE
                if (rideTripResponseData.cancelled_reason === "null" || rideTripResponseData.cancelled_reason === "") {
                    binding.cancelledReason.text = "Reason: N / A"
                } else {
                    binding.cancelledReason.text =
                        "Reason: " + rideTripResponseData.cancelled_reason
                }
                binding.status.text = "Cancelled"
                binding.status.setTextColor(ContextCompat.getColor(this, R.color.trip_cancelled))
            } else {
                binding.viewReceiptButton.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.VISIBLE
                binding.layoutBilldetails.visibility = View.VISIBLE
                binding.status.text = "Completed"
                binding.status.setTextColor(ContextCompat.getColor(this, R.color.acceptGreen))
            }

        }


    }

    override fun onClick(p0: View?) {
        val id = p0?.id

        when (id) {
            R.id.img_back_tripdetails -> {
                onBackPressed()
            }
            R.id.viewReceiptButton -> {
                if (rideTripResponseData.payment != null) {
                    showReceiptDialog(rideTripResponseData)
                }

            }

        }
    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun showReceiptDialog(rideTripResponseData: RideTripResponseData) {
        val receiptDialog = Dialog(this@TripDetailActivity)
        receiptDialog.setCancelable(true)
        receiptDialog.setCanceledOnTouchOutside(true)
        receiptDialog.setContentView(R.layout.dialog_receipt)
        receiptDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = receiptDialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        receiptDialog.show()
        val closeReceipt = receiptDialog.findViewById<Button>(R.id.closeReceipt)
        closeReceipt.setOnClickListener { receiptDialog.dismiss() }
        val bookingId = receiptDialog.findViewById<TextView>(R.id.bookingId)
        val distanceTravelled = receiptDialog.findViewById<TextView>(R.id.distanceTravelled)
        val basePrice = receiptDialog.findViewById<TextView>(R.id.basePrice)
        val distancePrice = receiptDialog.findViewById<TextView>(R.id.distancePrice)
        val TimeFare = receiptDialog.findViewById<TextView>(R.id.TimeFare)
        val TimeTaken = receiptDialog.findViewById<TextView>(R.id.TimeTaken)
        val taxPrice = receiptDialog.findViewById<TextView>(R.id.taxPrice)
        val totalAmount = receiptDialog.findViewById<TextView>(R.id.totalAmount)
        val subTotal = receiptDialog.findViewById<TextView>(R.id.subTotal)
        val discountprice = receiptDialog.findViewById<TextView>(R.id.discountprice)
        val txt_wallet_amount = receiptDialog.findViewById<TextView>(R.id.txt_wallet_amount)
        val wallet_amount_used_layout =
            receiptDialog.findViewById<LinearLayout>(R.id.wallet_amount_used_layout)
        val discount_layout = receiptDialog.findViewById<LinearLayout>(R.id.discount_layout)
        val tax_layout = receiptDialog.findViewById<LinearLayout>(R.id.tax_layout)
        bookingId.text = rideTripResponseData.booking_id
        distanceTravelled.text = "" + rideTripResponseData.payment?.distance_taken + " KM"
        basePrice.text =
            resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.fixed
        distancePrice.text =
            resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.distance_price
        TimeTaken.text = "" + rideTripResponseData.payment?.time_taken + " mins"
        TimeFare.text =
            resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.time_price
        subTotal.text =
            resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.trip_fare
        totalAmount.text =
            resources.getString(R.string.money_symbols) + "" + rideTripResponseData.payment?.total
        if (rideTripResponseData.payment?.discount?.toInt()!! > 0) {
            discount_layout.visibility = View.VISIBLE
            discountprice.text = String.format(
                "%s%d",
                resources.getString(R.string.money_symbols),
                (rideTripResponseData.payment.discount)
            )
        } else {
            discount_layout.visibility = View.GONE
        }
        if (rideTripResponseData.payment.tax?.toInt()!! > 0) {
            tax_layout.visibility = View.VISIBLE
            taxPrice.text = String.format(
                "%s%d",
                resources.getString(R.string.money_symbols),
                rideTripResponseData.payment.tax
            )
        } else {
            tax_layout.visibility = View.GONE
        }
        if (rideTripResponseData.payment.wallet?.toInt()!! > 0) {
            wallet_amount_used_layout.visibility = View.VISIBLE
            txt_wallet_amount.text = String.format(
                "%s%d",
                resources.getString(R.string.money_symbols),
                rideTripResponseData.payment.wallet
            )
        } else {
            wallet_amount_used_layout.visibility = View.GONE
        }
    }

}
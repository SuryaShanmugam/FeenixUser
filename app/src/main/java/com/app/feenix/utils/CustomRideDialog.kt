package com.app.feenix.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.eventbus.CancelRequestModel
import com.app.feenix.eventbus.GetMyLocationModel
import com.app.feenix.eventbus.MenuIconDisableModel
import com.app.feenix.view.ui.HomeActivity
import com.app.feenix.view.ui.referAndearn.ReferAndEarnActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.greenrobot.eventbus.EventBus

open class CustomRideDialog(context: Context) {
    private var dialog: Dialog? = null

    companion object {
        private var instance: CustomRideDialog? = null

        fun getInstance(context: Context): CustomRideDialog {
            if (instance == null)  // NOT thread safe!
                instance = CustomRideDialog(context)

            return instance!!
        }
    }

    @Suppress("DEPRECATION")
    fun showDialog(context: Context?) {
        try {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setContentView(R.layout.dialog_searching_drivers)
            val window: Window = dialog!!.window!!
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            window.setGravity(Gravity.CENTER)

            val cancelButton: Button = dialog!!.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                showCancelled(context)
                hideDialog()
            }
            dialog!!.show()
        } catch (e: Exception) {
        }
    }
    fun hideDialog() {
        dialog?.dismiss()
    }
    var TripCancelreason = ""
    private fun showCancelled(context: Context?) {

        val dialog: Dialog = BottomSheetDialog(context!!)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_cancel_reasons)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)
        dialog.show()

        val submitButton = dialog.findViewById<Button>(R.id.submitButton)


        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radiogroup_reason)

        val dialog_Cancel = dialog.findViewById<TextView>(R.id.dialog_Cancel)

        dialog_Cancel.setOnClickListener { dialog.dismiss() }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_bookedmistake -> TripCancelreason = "Booked by Mistake"
                R.id.radio_pickupincorrect -> TripCancelreason = "Pickup location incorrect"
                R.id.radio_driver_notmoving -> TripCancelreason = "Driver not moving towards me"
                R.id.radio_driver_cancel -> TripCancelreason = "Driver asked to cancel"
                R.id.radio_animaldenied -> TripCancelreason = "Wheelchair / Service animal denied"
                R.id.radio_anothercab -> TripCancelreason = "Hailed another Cab"
                R.id.radio_notlisted -> TripCancelreason = "Reason not listed"
            }
        }
        submitButton.setOnClickListener {
            if (!TripCancelreason.isEmpty()) {
                EventBus.getDefault().postSticky(CancelRequestModel(true,TripCancelreason,dialog))
            } else {
                ToastBuilder.build(context,"Please select any reason to cancel the trip")
            }
        }

    }

     fun showNotificationDialog(context: Context?,title:String,Message:String,isconfirmpickupactivity:Boolean) {

        val dialog: Dialog = Dialog(context!!)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_ride_notification)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
         window.setGravity(Gravity.CENTER)
        dialog.show()
        val notifcationText: TextView = dialog.findViewById<TextView>(R.id.notifcationText)
        val titleText: TextView = dialog.findViewById<TextView>(R.id.titleText)
        val dismissButton: Button = dialog.findViewById<Button>(R.id.dismissButton)
        val noti_icons: ImageView = dialog.findViewById<ImageView>(R.id.noti_icons)

         notifcationText.text = Message


        noti_icons.setImageDrawable(context.resources.getDrawable(R.drawable.ic_driver_not_found))

         titleText.text = title
        dismissButton.text = "OK"

        dismissButton.setOnClickListener {
            if(isconfirmpickupactivity)
            {
                EventBus.getDefault().postSticky(GetMyLocationModel(true))
                dialog.dismiss()
            }
            else{
                MyActivity.launchClearStack(context,HomeActivity::class.java)
                EventBus.getDefault().postSticky(MenuIconDisableModel(false))
                dialog.dismiss()
            }

        }
    }


// Invoice Details

    fun invoiceDetailDialog(context: Context?,fixed: Double?, distancePrice1: String?, timePrice: String?, distance: String?, timeTaken: String?, minimumFare: String?) {
        val dialog = Dialog(context!!)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_invoice_details)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)

        val baseFare = dialog.findViewById<TextView>(R.id.basePrice)
        val lblTimeFare = dialog.findViewById<TextView>(R.id.lblTimeFare)
        val distancePrice = dialog.findViewById<TextView>(R.id.distancePrice)
        val text_label_time = dialog.findViewById<TextView>(R.id.text_label_time)
        val txt_timetaken = dialog.findViewById<TextView>(R.id.txt_timetaken)
        val distanceTaken = dialog.findViewById<TextView>(R.id.distanceTaken)
        val lbl_minimumfare = dialog.findViewById<TextView>(R.id.lbl_minimumfare)
        val layout_minimumfare = dialog.findViewById<LinearLayout>(R.id.layout_minimumfare)
        val priceamount = fixed!!
        val distance_price1: Float = distancePrice1!!.toFloat()
        val time_price1: Float = timePrice!!.toFloat()

        baseFare.text =
            java.lang.String.format(
                "%s %s",
                context.resources.getString(R.string.money_symbols),
                String.format("%.1f", priceamount)
            )
        distancePrice.text =
            java.lang.String.format(
                "%s %s",
                context.resources.getString(R.string.money_symbols),
                String.format("%.1f", distance_price1)
            )
        lblTimeFare.text =
            java.lang.String.format(
                "%s %s",
                context.resources.getString(R.string.money_symbols),
                String.format("%.1f", time_price1)
            )
        distanceTaken.text = "$distance KMS"
        txt_timetaken.text = timeTaken + " Mins"

        if (minimumFare != null && minimumFare != "0") {
            layout_minimumfare.visibility = View.VISIBLE
            lbl_minimumfare.text = java.lang.String.format(
                "%s %s",
                context.resources.getString(R.string.money_symbols),
                minimumFare
            )
        } else {
            layout_minimumfare.visibility = View.GONE
        }
        dialog.show()
    }
    fun ShowThanksDialog(
        context: Context?,
        refferalcode: String,
        coordinatorLayoutHome: CoordinatorLayout,
        cardviewMylocationHome: CardView
    ) {
        val  thanksDialog = Dialog(context!!)
        thanksDialog.setCancelable(false)
        thanksDialog.setCanceledOnTouchOutside(false)
        thanksDialog.setContentView(R.layout.dialog_thanks)
        thanksDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window: Window = thanksDialog.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
        thanksDialog.show()

        val shareThanks: Button = thanksDialog.findViewById<Button>(R.id.shareThanks)
        val dissmiss: TextView = thanksDialog.findViewById<TextView>(R.id.dissmiss)
        val referralCode: TextView = thanksDialog.findViewById<TextView>(R.id.referralCode)
        val img_close: ImageView = thanksDialog.findViewById<ImageView>(R.id.img_close)
        referralCode.text = refferalcode
        img_close.setOnClickListener {
            thanksDialog.dismiss()
        }

        shareThanks.setOnClickListener {
            thanksDialog.dismiss()
            MyActivity.launch(context,ReferAndEarnActivity::class.java)
        }

        dissmiss.setOnClickListener {
            thanksDialog.dismiss()
            EventBus.getDefault().postSticky(MenuIconDisableModel(false))
            coordinatorLayoutHome.visibility= View.VISIBLE
            cardviewMylocationHome.visibility= View.VISIBLE
        }
    }

}
package com.app.feenix.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.*
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.app.feenix.R
import com.app.feenix.eventbus.CancelRequestModel
import com.app.feenix.eventbus.MenuIconDisableModel
import com.app.feenix.view.ui.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.greenrobot.eventbus.EventBus

open class CustomDriverSearchingDialog(context: Context) {
    private var dialog: Dialog? = null

    companion object {
        private var instance: CustomDriverSearchingDialog? = null

        fun getInstance(context: Context): CustomDriverSearchingDialog {
            if (instance == null)  // NOT thread safe!
                instance = CustomDriverSearchingDialog(context)

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

     fun showNotificationDialog(context: Context?,title:String,Message:String) {

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
            MyActivity.launchClearStack(context,HomeActivity::class.java)
            EventBus.getDefault().postSticky(MenuIconDisableModel(false))
            dialog.dismiss()
        }
    }



}
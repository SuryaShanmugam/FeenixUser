package com.app.feenix.view.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.feenix.R
import com.app.feenix.app.Constant
import com.app.feenix.databinding.ItemPaymentTypeRideBinding
import com.app.feenix.model.PaymentTypeModel
import com.bumptech.glide.Glide


class PaymentTypeAdapter(
    val context: Context, private var mTagsList: MutableList<PaymentTypeModel>,
    private val callback: PaymentTypeClickCallback
) :
    RecyclerView.Adapter<PaymentTypeAdapter.MyTodoListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemPaymentTypeRideBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), callback
        )


    override fun onBindViewHolder(holder: MyTodoListHolder, position: Int) {
        holder.bindViewHolder(mTagsList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = mTagsList.size
    inner class MyTodoListHolder(
        val mContext: Context,
        private val itemBinding: ItemPaymentTypeRideBinding,
        private val callback: PaymentTypeClickCallback
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bindViewHolder(promotionsData: PaymentTypeModel) {
            itemBinding.serviceItem.text = promotionsData.name
            if (promotionsData.type == 1) {
                Glide.with(context).load(R.drawable.ic_payment_cash).into(itemBinding.serviceImg)
                itemBinding.radioButton.visibility = View.VISIBLE
                itemBinding.checkBox.visibility = View.GONE
                if (promotionsData.isSelected) {

                    itemBinding.radioButton.isChecked = true
                    itemBinding.checkBox.isChecked = false
                } else {
                    itemBinding.radioButton.isChecked = false
                    itemBinding.checkBox.isChecked = false
                }
            } else if (promotionsData.type == 2) {
                Glide.with(context).load(R.drawable.ic_payment_card).into(itemBinding.serviceImg)
                itemBinding.radioButton.visibility = View.VISIBLE
                itemBinding.checkBox.visibility = View.GONE
            } else if (promotionsData.type == 3) {
                Glide.with(context).load(R.drawable.ic_payment_wallet).into(itemBinding.serviceImg)
                itemBinding.radioButton.visibility = View.GONE
                itemBinding.checkBox.visibility = View.VISIBLE
                if (promotionsData.isSelected) {
                    Constant.IS_SELECTED_WALLET = 0
                    Constant.PAYMENT_MODE = promotionsData.name
                    itemBinding.checkBox.isChecked = true
                    itemBinding.radioButton.isChecked = false
                } else {
                    itemBinding.checkBox.isChecked = false
                    itemBinding.radioButton.isChecked = false

                }
            }
            if (itemBinding.radioButton.isChecked) {
                Constant.IS_SELECTED_WALLET = 0
                Constant.PAYMENT_MODE = promotionsData.name
            }
            if (itemBinding.checkBox.isChecked) {
                Constant.IS_SELECTED_WALLET = 1
            } else {
                Constant.IS_SELECTED_WALLET = 0
            }

            itemBinding.cardLayout.setOnClickListener {
                it
                callback.onPaymentSelectItemClick(absoluteAdapterPosition, true)
            }


        }
    }

    var value = 0

    interface PaymentTypeClickCallback {
        fun onPaymentSelectItemClick(position: Int, isSelectedWallet: Boolean)
    }
}




package com.app.feenix.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ItemWalletTransactionBinding
import com.app.feenix.model.response.WalletTransactionList
import com.app.feenix.utils.Utils.getPaymentDate
import com.bumptech.glide.Glide


class WalletTransactionAdapter(
    val context: Context, private var mTagsList: ArrayList<WalletTransactionList>
) :
    RecyclerView.Adapter<WalletTransactionAdapter.MyTodoListHolder>() {
    var myPreference: MyPreference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemWalletTransactionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
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
        private val itemBinding: ItemWalletTransactionBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(mtagsdata: WalletTransactionList) {
            myPreference = MyPreference(mContext)
            itemBinding.txtNarration.text = mtagsdata.narration
            itemBinding.txtDate.text = getPaymentDate(mtagsdata.created_at)
            if (mtagsdata.type.equals("credit")) {
                Glide.with(mContext).load(R.drawable.ic_payment_credit)
                    .into(itemBinding.userImageIncoming)
                itemBinding.usernameLayout.visibility = View.GONE
            } else {
                Glide.with(mContext).load(R.drawable.ic_payment_debit)
                    .into(itemBinding.userImageIncoming)
                itemBinding.txtUsername.text = myPreference?.firstName
                itemBinding.usernameLayout.visibility = View.VISIBLE
            }


            if (mtagsdata.status.equals("1")) {
                itemBinding.txtStatus.text = "Success"
                itemBinding.txtStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.acceptGreen
                    )
                )
                itemBinding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_new_wallet_transaction_success,
                    0,
                    0,
                    0
                )
                itemBinding.txtStatus.compoundDrawablePadding = 12
            } else if (mtagsdata.status.equals("2")) {
                itemBinding.txtStatus.text = "Pending"
                itemBinding.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange))
                itemBinding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_wallet_transaction_pending,
                    0,
                    0,
                    0
                )
                itemBinding.txtStatus.compoundDrawablePadding = 12
            } else if (mtagsdata.status.equals("0")) {
                itemBinding.txtStatus.text = "Failed"
                itemBinding.txtStatus.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.acceptRed
                    )
                )
                itemBinding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_new_wallet_transaction_failed,
                    0,
                    0,
                    0
                )
                itemBinding.txtStatus.compoundDrawablePadding = 12
            }
        }
    }

}




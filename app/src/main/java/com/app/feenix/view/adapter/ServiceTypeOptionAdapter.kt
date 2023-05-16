package com.app.feenix.view.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.feenix.R
import com.app.feenix.databinding.ItemServiceTypeOptionBinding
import com.app.feenix.model.response.ServiceEstimationData
import com.bumptech.glide.Glide


class ServiceTypeOptionAdapter(
    val context: Context, private var mTagsList: MutableList<ServiceEstimationData>,
    private val callback: ServiceTypeItemClickCallback
) :
    RecyclerView.Adapter<ServiceTypeOptionAdapter.MyTodoListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemServiceTypeOptionBinding.inflate(
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
        private val itemBinding: ItemServiceTypeOptionBinding,
        private val callback: ServiceTypeItemClickCallback
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bindViewHolder(promotionsData: ServiceEstimationData) {
            itemBinding.serviceSeatCount.text = "4 Seats"
            itemBinding.serviceTitle.text = promotionsData.name
            itemBinding.txtTimes.text = promotionsData.time + " Mins"
            val priceamount: Float = promotionsData.estimated_fare!!.toFloat()
            itemBinding.serviceprice.text =
                context.getString(R.string.money_symbols) + String.format("%.0f", priceamount)

            itemBinding.serviceTitle.setTextColor(ContextCompat.getColor(context, R.color.primary))
            if (promotionsData.name.equals("Economy")) {
                Glide.with(context).load(R.drawable.ic_cartype_economy).into(itemBinding.carImage2)
            } else if (promotionsData.name.equals("Comfort")) {
                Glide.with(context).load(R.drawable.ic_cartype_comfort).into(itemBinding.carImage2)
            } else if (promotionsData.name.equals("Premium")) {
                Glide.with(context).load(R.drawable.ic_cartype_premium).into(itemBinding.carImage2)
            }
            if (promotionsData.isSelected) {
                itemBinding.highlightedLayout.setBackgroundResource(R.drawable.bg_servicetype_selected)
                itemBinding.serviceSeatCount.compoundDrawablePadding = 8
                callback.onServiceTypeDefaultSelectItemClick(absoluteAdapterPosition)

            } else {
                itemBinding.serviceTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.text_color
                    )
                )
                itemBinding.highlightedLayout.setBackgroundResource(R.drawable.bg_servicetype_unselected)
                itemBinding.highlightedLayout.background.alpha = 200
                itemBinding.serviceSeatCount.compoundDrawablePadding = 8
            }

            itemBinding.highlightedLayout.setOnClickListener(View.OnClickListener {
                callback.onServiceTypeSelectItemClick(absoluteAdapterPosition)
            })

        }
    }

    interface ServiceTypeItemClickCallback {
        fun onServiceTypeSelectItemClick(position: Int)
        fun onServiceTypeDefaultSelectItemClick(position: Int)
    }
}




package com.app.feenix.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.biu.model.ResponseModel.PromotionsData
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ItemPromotionBinding


class PromotionsAdapter(
    val context: Context, private var mTagsList: ArrayList<PromotionsData>,
    private val callback: PromotionCallback
) :
    RecyclerView.Adapter<PromotionsAdapter.MyTodoListHolder>() {
    var myPreference: MyPreference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemPromotionBinding.inflate(
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
    var mSelectedItem = -1
    var i = 0

    inner class MyTodoListHolder(
        val mContext: Context,
        private val itemBinding: ItemPromotionBinding,
        private val callback: PromotionCallback
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(promotionsData: PromotionsData) {
            myPreference = MyPreference(mContext)

            itemBinding.txtRadiobutton.isChecked = position == mSelectedItem
            if (promotionsData.promo_code != null) {
                itemBinding.txtPromocode.text = promotionsData.promo_code
            }

            val clickListener = View.OnClickListener {
                mSelectedItem = adapterPosition
                itemBinding.txtRadiobutton.isChecked = true
                notifyItemRangeChanged(0, mTagsList.size)
                callback.onItemClick(adapterPosition)
                i++
            }
            itemBinding.coordinatorLayout.setOnClickListener(clickListener)

        }
    }

    interface PromotionCallback {
        fun onItemClick(position: Int)
    }
}




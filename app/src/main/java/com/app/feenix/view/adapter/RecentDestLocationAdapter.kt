package com.app.feenix.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.feenix.databinding.ItemAutocompleteRowBinding
import com.app.feenix.model.response.RecentDestLocationData


class RecentDestLocationAdapter(
    val context: Context, private var mTagsList: MutableList<RecentDestLocationData>,
    private val callback: RecentDestItemClickCallback
) :
    RecyclerView.Adapter<RecentDestLocationAdapter.MyTodoListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemAutocompleteRowBinding.inflate(
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
        private val itemBinding: ItemAutocompleteRowBinding,
        private val callback: RecentDestItemClickCallback
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(promotionsData: RecentDestLocationData) {
            itemBinding.placeName.text = promotionsData.d_title
            itemBinding.placeDetail.text = promotionsData.d_address
            itemBinding.itemRecentdestRoot.setOnClickListener(View.OnClickListener {
                callback.onRecentDestItemClick(absoluteAdapterPosition)
            })

        }
    }

    interface RecentDestItemClickCallback {
        fun onRecentDestItemClick(position: Int)
    }
}




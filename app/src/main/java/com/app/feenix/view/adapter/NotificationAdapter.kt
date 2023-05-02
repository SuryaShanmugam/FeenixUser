package com.app.feenix.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.biu.model.ResponseModel.NotificationData
import com.app.feenix.databinding.ItemNotificationBinding
import com.app.feenix.utils.Log
import com.app.feenix.utils.Utils.tripsDateformat


class NotificationAdapter(
    val context: Context, private var mTagsList: ArrayList<NotificationData>,
) :
    RecyclerView.Adapter<NotificationAdapter.MyTodoListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemNotificationBinding.inflate(
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
        private val itemBinding: ItemNotificationBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(notifications: NotificationData) {
            Log.error("frertgert", "" + notifications.toString())

            if (notifications.title != null) {
                itemBinding.txtTitle.text = notifications.title
            }
            if (notifications.message != null) {
                itemBinding.txtShortdesc.text = notifications.message
            }
            if (notifications.created_at != null) {
                itemBinding.txtDate.text = tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "EEE, MMM dd", notifications.created_at
                )
            }
        }
    }

}




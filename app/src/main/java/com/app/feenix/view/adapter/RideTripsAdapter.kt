package com.app.feenix.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.biu.model.RequestModel.ResponseModel.RideTripResponseData
import com.app.feenix.R
import com.app.feenix.databinding.ItemRideTripsBinding
import com.app.feenix.utils.Utils
import com.bumptech.glide.Glide


class RideTripsAdapter(
    val context: Context, private var mTagsList: ArrayList<RideTripResponseData>,
    private val callback: TagsClickCallback
) :
    RecyclerView.Adapter<RideTripsAdapter.MyTodoListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTodoListHolder =
        MyTodoListHolder(
            parent.context, ItemRideTripsBinding.inflate(
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
        private val itemBinding: ItemRideTripsBinding,
        private val callback: TagsClickCallback
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindViewHolder(mtagsdata: RideTripResponseData) {

            Glide.with(mContext).load(mtagsdata.provider?.avatar)
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.taxi_placeholder))
                .dontAnimate().into(itemBinding.bookingImage)
            itemBinding.destination.text = mtagsdata.d_address
            if (mtagsdata.provider_profiles?.car_registration != null) {
                itemBinding.carnumber.text = mtagsdata.provider_profiles.car_registration
            }
            itemBinding.carName.text = mtagsdata.service_type?.name
            itemBinding.drivername.text = mtagsdata.provider?.first_name
            val rating: Float = mtagsdata.provider?.rating!!.toFloat()
            itemBinding.ratingText.text = String.format("%.0f", rating)


            if (mtagsdata.status.equals("CANCELLED")) {
                itemBinding.dateTime.text = Utils.tripsDateformat(
                    "yyyy-MM-dd HH:mm:ss",
                    "EEE, MMM dd , hh:mm aaa", mtagsdata.assigned_at
                )
                itemBinding.status.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.trip_cancelled
                    )
                )
                itemBinding.status.text = "Cancelled"
            } else {
                itemBinding.price.text =
                    mContext.resources.getString(R.string.money_symbols) + mtagsdata.total
                if (mtagsdata.finished_at != null) {
                    itemBinding.dateTime.text = Utils.tripsDateformat(
                        "yyyy-MM-dd HH:mm:ss",
                        "EEE, MMM dd , hh:mm aaa",
                        mtagsdata.finished_at
                    )
                }

                itemBinding.status.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.acceptGreen
                    )
                )
                itemBinding.status.text = "Completed"
            }

            itemView.setOnClickListener { v ->
                callback.onTagsClickCallback(adapterPosition)
            }
        }
    }

    interface TagsClickCallback {
        fun onTagsClickCallback(position: Int)
    }

}




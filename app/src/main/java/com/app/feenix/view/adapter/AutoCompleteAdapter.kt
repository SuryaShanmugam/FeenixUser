package com.app.feenix.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.feenix.R
import com.app.feenix.utils.customcomponents.PlaceAutoComplete
import java.util.*

class AutoCompleteAdapter(
    private val context: Context,
    private val arrayList: ArrayList<PlaceAutoComplete>
) : BaseAdapter() {
    private lateinit var placename: TextView
    private lateinit var placedescrition: TextView
    var Place: PlaceAutoComplete? = null
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView =
            LayoutInflater.from(context).inflate(R.layout.item_autocomplete_row, parent, false)
        placename = convertView.findViewById(R.id.place_name)
        placedescrition = convertView.findViewById(R.id.place_detail)

        /***** Get each Model object from ArrayList  */
        Place = arrayList.get(position)
        val st = StringTokenizer(Place?.description, ",")

        placename.text = st.nextToken()
        placedescrition.text = arrayList[position].description
        return convertView
    }
}


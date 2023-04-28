package com.app.feenix.view.activities.tripdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.MyActivity
import com.app.biu.model.RequestModel.ResponseModel.RideTripResponse
import com.app.biu.model.RequestModel.ResponseModel.RideTripResponseData
import com.app.feenix.databinding.FragmentRidetripsBinding
import com.app.feenix.feature.internet.InternetConnectionLayout
import com.app.feenix.view.activities.base.BaseActivity
import com.app.feenix.view.activities.base.BaseFragment
import com.app.feenix.view.adapter.RideTripsAdapter
import com.app.feenix.viewmodel.IYourTripsData
import com.app.feenix.webservices.yourTrips.YourTripService

class DeliveryTripsFragment : BaseFragment(), IYourTripsData, RideTripsAdapter.TagsClickCallback {

    private var mContext: Context? = null
    private lateinit var binding: FragmentRidetripsBinding
    private lateinit var rideTripsAdapter: RideTripsAdapter
    private var yourTripService: YourTripService? = null
    private var mRideList: ArrayList<RideTripResponseData> = arrayListOf()
    private lateinit var internetConnectionLayout: InternetConnectionLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRidetripsBinding.inflate(inflater, container, false)
        initObjects()
        initRecyclerview()
        return binding.root
    }

    private fun initRecyclerview() {

        binding.rcRidetrips.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rcRidetrips.adapter = rideTripsAdapter
        binding.rcRidetrips.isNestedScrollingEnabled
    }

    private fun initObjects() {
        mContext = activity
        internetConnectionLayout = binding.fragmentAlarmListInternetConnectionLayout.root
        internetConnectionLayout.init(activity as BaseActivity)
        yourTripService = YourTripService()
        yourTripService!!.YourTripService(mContext!!)
        rideTripsAdapter = RideTripsAdapter(mContext!!, mRideList, this)
        if (hasInternetConnection()) {
            yourTripService?.getRideTrips(this)
        }
    }

    override fun onResume() {
        super.onResume()
        internetConnectionLayout.apply {
            onResume()
            registerInternetConnectionListener("RideTripsFragment")
        }
    }

    override fun onPause() {
        internetConnectionLayout.unregisterInternetConnectionListener()
        super.onPause()
    }

    override fun onRideTripResponse(rideTripResponse: RideTripResponse) {
        if (rideTripResponse.success!!) {
            mRideList.clear()
            if (rideTripResponse.data.size > 0) {
                for (Rdata in rideTripResponse.data) {
                    if (Rdata.service_type?.is_delivery == 1) {

                        mRideList.add(Rdata)
                        rideTripsAdapter.notifyDataSetChanged()
                    }
                }
                binding.rcRidetrips.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE

            } else {
                binding.rcRidetrips.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
            }

        }


    }

    override fun onTagsClickCallback(position: Int) {
        val response = mRideList.get(position)
        val bundle = Bundle()
        bundle.putParcelable("RideDetails", response)
        MyActivity.launchWithBundle(mContext!!, TripDetailActivity::class.java, bundle)

    }


}
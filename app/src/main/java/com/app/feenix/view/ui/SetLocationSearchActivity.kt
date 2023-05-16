package com.app.feenix.view.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import cbs.com.bmr.Utilities.ToastBuilder
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.app.feenix.R
import com.app.feenix.app.AppController
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySetLocationSearchBinding
import com.app.feenix.model.request.AddLocation
import com.app.feenix.model.response.GetLocationResponse
import com.app.feenix.model.response.GetPriceEstimationResponse
import com.app.feenix.model.response.GetServiceEstimationResponse
import com.app.feenix.utils.customcomponents.CustomAutoCompleteListView
import com.app.feenix.utils.customcomponents.PlacePredictions
import com.app.feenix.view.adapter.AutoCompleteAdapter
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.IBookingRides
import com.app.feenix.webservices.bookingride.BookingRideService
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class SetLocationSearchActivity : BaseActivity(), View.OnClickListener, IBookingRides {

    private lateinit var binding: ActivitySetLocationSearchBinding
    private var mContext: Context? = null
    private lateinit var myPreference: MyPreference

    // Places Initialise
    var placesClient: PlacesClient? = null
    var bookingRideService: BookingRideService? = null
    var type: Int? = null
    var current_lat: Double? = null
    var current_lng: Double? = null
    var LocationType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentValues()
        initObject()
        initCallbacks()
    }

    private fun initCallbacks() {
        binding.imgBackSetlocation.setOnClickListener(this)

    }

    private fun getIntentValues() {
        val bundle = intent.extras
        if (bundle != null) {
            type = bundle.getInt("from")
            current_lat = bundle.getDouble("CurrentLat")
            current_lng = bundle.getDouble("CurrentLng")
            LocationType = bundle.getString("locationtype")
        }

    }

    private var predictions: PlacePredictions = PlacePredictions()
    private fun initObject() {
        mContext = this
        myPreference = MyPreference(mContext!!)
        Places.initialize(mContext!!, myPreference.dynamicMapkey!!)
        placesClient = Places.createClient(mContext!!)
        bookingRideService = BookingRideService()
        bookingRideService?.BookingRideService(mContext!!)
        binding.editSearchlocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(sValue: Editable) {
                if (sValue.toString().length > 0) {
                    binding.listviewAutocompelete.visibility = View.VISIBLE
                    CustomAutoCompleteListView.getInstance(mContext!!).searchPlaces(
                        sValue.toString(),
                        mContext!!, current_lat, current_lng, myPreference.dynamicMapkey!!,
                        binding.listviewAutocompelete
                    )

                } else {
                    binding.listviewAutocompelete.visibility = View.GONE
                }
            }
        })

        binding.listviewAutocompelete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> // pass the result to the calling activity
                val placeId: String = predictions.getPlaces()!!.get(position).place_id!!
                val placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS_COMPONENTS
                )
                val request = FetchPlaceRequest.builder(placeId, placeFields)
                    .build()
                placesClient!!.fetchPlace(request).addOnSuccessListener { response ->
                    val place = response.place
                    binding.listviewAutocompelete.visibility = View.GONE
                    addLocation(place)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        val apiException = exception
                        val statusCode = apiException.statusCode
                    }
                }
            }


    }

    private fun addLocation(place: Place) {
        if (hasInternetConnection()) {
            bookingRideService?.AddLocation(
                this, AddLocation(
                    place.latLng?.latitude, place.latLng?.longitude, place.name,
                    place.address, LocationType
                )
            )
        } else {
            ToastBuilder.build(mContext!!, "No Internet, Please try Again")
        }

    }

    private var mAutoCompleteAdapter: AutoCompleteAdapter? = null

    fun searchPlaces(ValuesEditext: String) {
        val `object` = JSONObject()
        if (ValuesEditext.length > 3) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                CustomAutoCompleteListView.getInstance(mContext!!).getPlaceAutoCompleteUrl(
                    ValuesEditext, current_lat, current_lng, myPreference.dynamicMapkey
                ),
                `object`, { response ->
                    val gson = Gson()
                    predictions =
                        gson.fromJson(response.toString(), PlacePredictions::class.java)
                    if (mAutoCompleteAdapter == null) {
                        binding.listviewAutocompelete.visibility = View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            this@SetLocationSearchActivity,
                            predictions.getPlaces()!!
                        )
                        binding.listviewAutocompelete.adapter = mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                    } else {
                        binding.listviewAutocompelete.visibility = View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            this@SetLocationSearchActivity,
                            predictions.getPlaces()!!
                        )
                        binding.listviewAutocompelete.adapter = mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                        binding.listviewAutocompelete.invalidate()
                    }
                }
            ) { error -> Log.v("SearchResponse", error.toString()) }
            AppController.applicationInstance.addToRequestQueue(jsonObjectRequest)
        }
    }


    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.img_back_setlocation -> {
                onBackPressed()
            }

        }

    }

    override fun ongetSavedLocationsHome(getLocationResponse: GetLocationResponse) {
        if (getLocationResponse.success) {
            ToastBuilder.build(mContext!!, "Location Added Successfully")
            finishIntent(this)
        }
    }

    override fun onGetServiceTypeEstimation(getServiceEstimationResponse: GetServiceEstimationResponse) {

    }

    override fun onGetPriceEstimation(getPriceEstimationResponse: GetPriceEstimationResponse) {


    }

    fun finishIntent(activity: Activity) {
        val intent = Intent()
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

}
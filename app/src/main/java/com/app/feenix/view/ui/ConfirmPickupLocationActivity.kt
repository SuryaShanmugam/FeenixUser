package com.app.feenix.view.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.android.volley.toolbox.JsonObjectRequest
import com.app.feenix.R
import com.app.feenix.app.AppController
import com.app.feenix.app.Constant
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivityConfirmPickupLocationBinding
import com.app.feenix.eventbus.CancelRequestModel
import com.app.feenix.eventbus.NoDriversFoundModel
import com.app.feenix.model.request.CancelRideRequest
import com.app.feenix.model.request.SendRideRequest
import com.app.feenix.model.response.CancelRideResponse
import com.app.feenix.model.response.SendRideResponse
import com.app.feenix.utils.CustomDriverSearchingDialog
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.ICallback
import com.app.feenix.viewmodel.ISendRideRequest
import com.app.feenix.webservices.bookingride.BookingRideService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ConfirmPickupLocationActivity : BaseActivity(), View.OnClickListener,
    OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener ,ISendRideRequest{

    private lateinit var binding: ActivityConfirmPickupLocationBinding
    private var mContext: Context? = null
    private lateinit var myPreference: MyPreference

    // Places Initialise
    var placesClient: PlacesClient? = null
    var bookingRideService: BookingRideService? = null
    var type: Int? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationRequest: LocationRequest? = null
    var mapFragment: SupportMapFragment? = null
    var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPickupLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObject()
        initCallbacks()
    }

    private fun initObject() {
        mContext = this
        myPreference = MyPreference(mContext!!)
        Places.initialize(mContext!!, myPreference.dynamicMapkey!!)
        placesClient = Places.createClient(mContext!!)
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            settingsrequest()
            initlocationRequest()
        }

        binding.txtLableDestination.text = Constant.DEST_TITLE
        binding.txtLableSource.text = Constant.SOURCE_TITLE
        initSaveFuture()
        bookingRideService= BookingRideService()
        bookingRideService?.BookingRideService(mContext!!)

    }

    var isSaveFutureChecked = false
    private fun initSaveFuture() {

        binding.checkBoxSaveFuture.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isSaveFutureChecked = true
                binding.addressType.visibility = View.VISIBLE
            } else {
                isSaveFutureChecked = false
                binding.addressType.visibility = View.GONE
                binding.titleLayout.visibility = View.GONE
            }
        }
    }

    private fun initCallbacks() {
        binding.imgConfirmpickupBack.setOnClickListener(this)
        binding.imgMylocation.setOnClickListener(this)
        binding.homeLayout.setOnClickListener(this)
        binding.workLayout.setOnClickListener(this)
        binding.otherLayout.setOnClickListener(this)
        binding.confirmButton.setOnClickListener(this)
        binding.txtAddNotesDriver.setOnClickListener(this)
        binding.txtSelectedPickupnote.setOnClickListener(this)

    }

    private var clickedLayout = "1"
    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.img_confirmpickup_back -> {
                onBackPressed()
            }
            R.id.img_mylocation -> {
                getMyLocation()
            }
            R.id.homeLayout -> {
                clickedLayout = "1"
                binding.titleLayout.visibility = View.GONE
                binding.homeLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.dotted_linegrey_rectangle
                    )
                binding.workLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
                binding.otherLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
            }
            R.id.workLayout -> {
                clickedLayout = "2"
                binding.titleLayout.visibility = View.GONE
                binding.workLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.dotted_linegrey_rectangle
                    )
                binding.homeLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
                binding.otherLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
            }
            R.id.otherLayout -> {
                clickedLayout = "3"
                binding.titleLayout.visibility = View.VISIBLE
                binding.otherLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.dotted_linegrey_rectangle
                    )
                binding.homeLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
                binding.workLayout.background =
                    ContextCompat.getDrawable(
                        this@ConfirmPickupLocationActivity,
                        R.drawable.bg_confimlocation_unslected
                    )
            }
            R.id.confirmButton -> {
                if(hasInternetConnection())
                {
                    Log.e("dfwefrwde",""+getRequestParams().toString())
                   bookingRideService?.sendRideRequest(this,getRequestParams())
                }else
                {
                    ToastBuilder.build(mContext!!,"No Internet, Please try Again")
                }


            }
            R.id.txt_add_Notes_Driver -> {
                dialogPickupNotes()
            }
            R.id.txt_selected_pickupnote -> {
                binding.txtSelectedPickupnote.text = ""
                binding.txtSelectedPickupnote.visibility = View.GONE
                binding.txtAddNotesDriver.visibility = View.VISIBLE
            }
        }
    }

    private fun getRequestParams() :SendRideRequest{
            val SendRideRequest= SendRideRequest(
                s_latitude=Constant.SOURCE_LAT ,
                s_longitude=Constant.SOURCE_LNG,
                d_latitude=Constant.DEST_LAT,
                d_longitude=Constant.DEST_LNG,
                service_type=Constant.SERVICE_TYPE,
                schedule_date="",
                schedule_time="",
                use_wallet=Constant.IS_SELECTED_WALLET,
                payment_mode=Constant.PAYMENT_MODE,
                s_title= Constant.SOURCE_TITLE,
                s_address=Constant.SOURCE_ADDRESS,
                    d_title=Constant.DEST_TITLE,
                    d_address=Constant.DEST_ADDRESS,
                    card_id="",
                    receiver_name="",
                    receiver_mobile="",
                    pickup_instruction="",
                    delivery_instruction="",
                    package_details="",
                    package_type="",
                    estimated_fare=Constant.ESTIMATED_FARE.toString(),
                    distance=Constant.DISTANCE.toString(),
                    distance_price=Constant.DISTANCE_PRICE,
                    time=Constant.TIME,
                    time_price=Constant.TIME_PRICE,
                    tax_price=Constant.TAX_PRICE,
                    base_price=Constant.BASE_PRICE,
                    wallet_balance=Constant.WALLET_BAL,
                    discount=Constant.DISCOUNT.toString(),
                    pickup_note=binding.txtSelectedPickupnote.text.toString()
            )

        return SendRideRequest
    }

    private fun getMyLocation() {
        try {
            val latLng = LatLng(map!!.myLocation.latitude, map!!.myLocation.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f)
            map!!.animateCamera(cameraUpdate)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    var dialogPickupNotesDialog: Dialog? = null
    private fun dialogPickupNotes() {
        dialogPickupNotesDialog = BottomSheetDialog(this@ConfirmPickupLocationActivity)
        dialogPickupNotesDialog?.setCancelable(true)
        dialogPickupNotesDialog?.setCanceledOnTouchOutside(true)
        dialogPickupNotesDialog?.setContentView(R.layout.dialog_pickupnote)
        dialogPickupNotesDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window: Window = dialogPickupNotesDialog?.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)
        dialogPickupNotesDialog?.show()

        val txt_cancel: TextView? = dialogPickupNotesDialog?.findViewById<TextView>(R.id.txt_cancel)
        val edit_pickUpInstruction: EditText? =
            dialogPickupNotesDialog?.findViewById<EditText>(R.id.edit_pickUpInstruction)
        val txt_gatecode: TextView? =
            dialogPickupNotesDialog?.findViewById<TextView>(R.id.txt_gatecode)
        val txt_im_wearing: TextView? =
            dialogPickupNotesDialog?.findViewById<TextView>(R.id.txt_im_wearing)
        val txt_in_frontof: TextView? =
            dialogPickupNotesDialog?.findViewById<TextView>(R.id.txt_in_frontof)
        val btn_done: Button? = dialogPickupNotesDialog?.findViewById<Button>(R.id.btn_done)

        txt_cancel?.setOnClickListener { dialogPickupNotesDialog?.dismiss() }
        txt_gatecode?.setOnClickListener { edit_pickUpInstruction?.setText(txt_gatecode.text.toString()) }
        txt_im_wearing?.setOnClickListener { edit_pickUpInstruction?.setText(txt_im_wearing.text.toString()) }
        txt_in_frontof?.setOnClickListener { edit_pickUpInstruction?.setText(txt_in_frontof.text.toString()) }

        edit_pickUpInstruction?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edit_pickUpInstruction.setBackgroundResource(R.drawable.bg_edittext_selected)
                btn_done?.setBackgroundResource(R.drawable.bg_rect_primary)
                btn_done?.setTextColor(
                    ContextCompat.getColor(
                        this@ConfirmPickupLocationActivity,
                        R.color.black
                    )
                )
                edit_pickUpInstruction.setSelection(edit_pickUpInstruction.text.toString().length)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        btn_done?.setOnClickListener {
            if (!edit_pickUpInstruction?.text.toString().isEmpty()) {
                PickupNote = edit_pickUpInstruction?.text.toString()
                binding.txtSelectedPickupnote.text = PickupNote
                dialogPickupNotesDialog?.dismiss()
                binding.txtAddNotesDriver.visibility = View.GONE
                binding.txtSelectedPickupnote.visibility = View.VISIBLE
            } else {
                ToastBuilder.build(mContext!!, "Pickup Note Added")
            }
        }

    }

    var PickupNote = ""
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map!!.isMyLocationEnabled = true
        map!!.isBuildingsEnabled = true
        map!!.setOnMyLocationChangeListener { location ->
            if (value == 0) {
                val myLocation = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder().target(myLocation).zoom(15f).build()
                map!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                map!!.setPadding(0, 0, 0, 0)
                map!!.uiSettings.isZoomControlsEnabled = false
                map!!.uiSettings.isMyLocationButtonEnabled = true
                map!!.uiSettings.isMapToolbarEnabled = false
                map!!.uiSettings.isCompassEnabled = false
                value++
            }
        }

        SetonMap()
    }

    var value = 0
    var SetonMapTitle1: String? = null
    var SetonMapTitle3: String? = null
    var SetonMapTitle2: String? = null
    private fun SetonMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        map!!.isMyLocationEnabled = true
        map!!.isBuildingsEnabled = true
        map!!.setOnCameraMoveStartedListener {
            binding.editAddress.setText("Fetching...")
        }
        map!!.setOnCameraIdleListener {
            getCompleteAdddressString(
                this@ConfirmPickupLocationActivity,
                map!!.cameraPosition.target.latitude,
                map!!.cameraPosition.target.longitude,
                object : ICallback {
                    override fun onSuccess(result: JSONObject?) {
                        currentLatitude = map!!.cameraPosition.target.latitude
                        currentLongitude = map!!.cameraPosition.target.longitude
                        var Results: JSONArray? = null
                        try {
                            Results = result?.getJSONArray("results")
                            val zero = Results?.getJSONObject(0)
                            val address_components = zero
                                ?.getJSONArray("address_components")
                            for (i in 0 until address_components!!.length()) {
                                val zero2 = address_components
                                    .getJSONObject(i)
                                val long_name = zero2?.getString("long_name")
                                val mtypes = zero2?.getJSONArray("types")
                                for (k in 0 until mtypes!!.length()) {
                                    if (mtypes[k].toString()
                                            .equals("sublocality", ignoreCase = true)
                                    ) {
                                        SetonMapTitle1 = address_components.getJSONObject(i)
                                            .getString("long_name")
                                    }
                                    if (mtypes[k].toString()
                                            .equals("locality", ignoreCase = true)
                                    ) {
                                        SetonMapTitle2 = address_components.getJSONObject(i)
                                            .getString("long_name")
                                    }
                                    if (mtypes[k].toString().equals(
                                            "administrative_area_level_1",
                                            ignoreCase = true
                                        )
                                    ) {
                                        SetonMapTitle3 = address_components.getJSONObject(i)
                                            .getString("long_name")
                                    }
                                }
                            }
                            val strAdd = (result?.get("results") as JSONArray).getJSONObject(0)
                                .getString("formatted_address")
                            Log.e(
                                "addressewqweqw",
                                "" + (result["results"] as JSONArray).getJSONObject(0)
                                    .getString("address_components")
                            )
                            binding.editAddress.setText(strAdd)
                            binding.txtLableSource.text = strAdd
                            Constant.SOURCE_LAT= currentLatitude
                            Constant.SOURCE_LNG= currentLongitude
                            Constant.SOURCE_ADDRESS= strAdd
                            Constant.SOURCE_TITLE= strAdd
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                })
        }


    }

    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    fun getCompleteAdddressString(
        context: Context?,
        LATITUDE: Double,
        LONGITUDE: Double,
        callback: ICallback
    ) {
        val lat = LATITUDE.toString()
        val lngg = LONGITUDE.toString()
        val myPreference = MyPreference(context!!)
        val resultt =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lngg +
                    "&sensor=true&key=" + myPreference.dynamicMapkey
        val jsonObjectRequest = JsonObjectRequest(resultt,
            null,
            { jsonObject ->
                callback.onSuccess(jsonObject)
            },
            { error -> Log.d("getjson", "onResponse: $error") })
        AppController.applicationInstance.addToRequestQueue(jsonObjectRequest)
    }


    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (location == null) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest!!, this
                )
            } catch (e: Exception) {
            }


        } else {
            currentLatitude = location.latitude
            currentLongitude = location.longitude

        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location) {
        val lat: Double = Constant.SOURCE_LAT
        val lng: Double = Constant.SOURCE_LNG
        val latLng = LatLng(lat, lng)
        val center = CameraUpdateFactory.newLatLng(latLng)
        val zoom = CameraUpdateFactory.zoomTo(15f)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map!!.isMyLocationEnabled = true
        map!!.moveCamera(center)
        map!!.animateCamera(zoom)

        currentLatitude = location.latitude
        currentLongitude = location.longitude
    }

    private fun initlocationRequest() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong()) // 10 seconds, in milliseconds
            .setFastestInterval((1 * 1000).toLong())
    }

    fun settingsrequest() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (30 * 1000).toLong()
        locationRequest.fastestInterval = (5 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this is the key ingredient
        val result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            val state = result.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {}
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    try {
                        status.startResolutionForResult(this@ConfirmPickupLocationActivity, 5)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    override fun onsendRideResponse(sendRideResponse: SendRideResponse) {
        if(sendRideResponse.success!=null && sendRideResponse.success)
        {
            myPreference.TripSearchingStatus="true"
            myPreference.CurrentRequestId=sendRideResponse.request_id
            MyActivity.launchClearStack(mContext!!,HomeActivity::class.java)
        }
        else if(sendRideResponse.error!=null &&sendRideResponse.error )
        {
            myPreference.TripSearchingStatus="false"
            myPreference.CurrentRequestId=""
            CustomDriverSearchingDialog.getInstance(mContext!!).showNotificationDialog(mContext!!,
                "No Driver", sendRideResponse.message!!)
        }


    }

    override fun onCancelRideResponse(cancelRideResponse: CancelRideResponse) {
        if(cancelRideResponse.success!!)
        {
            ToastBuilder.build(mContext!!,cancelRideResponse.message)
            MyActivity.launchClearTop(mContext!!,HomeActivity::class.java)
        }

    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CancelRequestModel) {
        bookingRideService?.CancelRideRequest(this, CancelRideRequest(myPreference.CancelledRequest,
        event.reason))
        event.dialog.dismiss()
        EventBus.getDefault().removeStickyEvent(CancelRequestModel::class.java)
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NoDriversFoundModel) {
        if (event.type.equals("No Driver", ignoreCase = true)) {
            CustomDriverSearchingDialog.getInstance(mContext!!).showNotificationDialog(mContext!!,
                event.type,event.message)
            myPreference.CurrentRequestId=""
        }
        EventBus.getDefault().removeStickyEvent(NoDriversFoundModel::class.java)

    }
}


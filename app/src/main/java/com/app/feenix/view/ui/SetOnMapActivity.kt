package com.app.feenix.view.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.app.feenix.R
import com.app.feenix.app.AppController
import com.app.feenix.app.Constant
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.ActivitySetOnMapBinding
import com.app.feenix.view.ui.base.BaseActivity
import com.app.feenix.viewmodel.ICallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SetOnMapActivity : BaseActivity(), View.OnClickListener,
    OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private lateinit var binding: ActivitySetOnMapBinding

    var mContext: Context? = null
    lateinit var myPreference: MyPreference

    // Places Initialise
    var placesClient: PlacesClient? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationRequest: LocationRequest? = null
    var mapFragment: SupportMapFragment? = null
    var map: GoogleMap? = null
    var type: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentValues()

        initlocation()
        initObjects()
        initCallbacks()
    }

    private fun getIntentValues() {
        val bundle = intent.extras
        if (bundle != null) {
            type = bundle.getInt("from")
        }

    }

    private fun initlocation() {
        checkCallingOrSelfPermission("")
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    private fun initCallbacks() {
        binding.imgBackSetonmap.setOnClickListener(this)
        binding.confirmButton.setOnClickListener(this)
        binding.closeIcon.setOnClickListener(this)

    }

    private fun initObjects() {
        mContext = this@SetOnMapActivity
        myPreference = MyPreference(mContext!!)
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        Places.initialize(mContext!!, myPreference.dynamicMapkey!!)
        placesClient = Places.createClient(mContext!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            settingsrequest()
            initlocationRequest()
        }

    }

    private fun initlocationRequest() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((10 * 1000).toLong()) // 10 seconds, in milliseconds
            .setFastestInterval((1 * 1000).toLong())
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.img_back_setonmap -> {
                onBackPressed()
            }
            R.id.confirmButton -> {
                handleSearchClick(
                    binding.searchBox.text.toString(),
                    binding.searchBox.text.toString(),
                    currentLatitude,
                    currentLongitude,
                    this
                )
            }
            R.id.closeIcon -> {
                binding.searchBox.text.clear()
            }
        }

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
                        status.startResolutionForResult(this@SetOnMapActivity, 5)
                    } catch (e: SendIntentException) {
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val style = MapStyleOptions.loadRawResourceStyle(mContext!!, R.raw.map_style)
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
                val myLocation =
                    LatLng(location.latitude, location.longitude)
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

    private var value = 0
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
            binding.searchBox.setText("Search on Location")
        }
        map!!.setOnCameraIdleListener {
            getCompleteAdddressString(
                this@SetOnMapActivity,
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
                            binding.searchBox.setText(strAdd)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                })
        }


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
            if (currentLatitude != 0.0 && currentLongitude != 0.0) {

//                    setlocation();
            }
        }
    }

    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
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

    fun handleSearchClick(
        address: String?,
        title: String?,
        latitude: Double?,
        longitude: Double?,
        activity: Activity
    ) {
        binding.searchBox.setText(address)
        val intent = Intent()
        intent.putExtra("daddress", address)
        intent.putExtra("dtitle", title)
        intent.putExtra("d_lat", latitude)
        intent.putExtra("d_lng", longitude)
        activity.setResult(RESULT_OK, intent)
        activity.finish()

    }
}
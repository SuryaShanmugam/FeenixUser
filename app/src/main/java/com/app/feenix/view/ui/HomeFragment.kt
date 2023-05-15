package com.app.feenix.view.ui

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cbs.com.bmr.Utilities.MyActivity
import cbs.com.bmr.Utilities.ToastBuilder
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.app.feenix.R
import com.app.feenix.app.AppController
import com.app.feenix.app.Constant
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.FragmentHomeBinding
import com.app.feenix.databinding.LayoutHomeBottomBinding
import com.app.feenix.databinding.LayoutHomeServiceTypeBinding
import com.app.feenix.eventbus.MenuIconDisableModel
import com.app.feenix.eventbus.OnHomeLocationEnableModel
import com.app.feenix.model.response.GetLocationData
import com.app.feenix.model.response.GetLocationResponse
import com.app.feenix.model.response.RecentDestLocationData
import com.app.feenix.utils.Utils
import com.app.feenix.utils.customcomponents.CustomAutoCompleteListView
import com.app.feenix.utils.customcomponents.PlacePredictions
import com.app.feenix.view.adapter.AutoCompleteAdapter
import com.app.feenix.view.adapter.RecentDestLocationAdapter
import com.app.feenix.viewmodel.IBookingRides
import com.app.feenix.webservices.bookingride.BookingRideService
import com.directions.route.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.gson.Gson
import io.intercom.android.sdk.utilities.KeyboardUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, View.OnClickListener, IBookingRides,
    RecentDestLocationAdapter.RecentDestItemClickCallback, RoutingListener {
    private var mContext: Context? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var myPreference: MyPreference
    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mylocationButton: CardView
    private var bookingRideService: BookingRideService? = null

    // Places Initialise
    var placesClient: PlacesClient? = null
    lateinit var sourceLayout: LayoutHomeBottomBinding
    lateinit var servicetypeLayout: LayoutHomeServiceTypeBinding


    var current_lat: Double? = null
    var current_lng: Double? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initObjects()
        initCallbacks()
        initMaps()
        initBottomSheetAnimations()
        backpressHandler()

        return binding.root
    }




    private fun initObjects() {
        mContext = activity
        myPreference = MyPreference(mContext!!)
        sourceLayout = binding.sourceLayout
        servicetypeLayout = binding.servicetypeLayout
        mylocationButton = sourceLayout.cardviewMylocationHome
        Places.initialize(mContext!!, myPreference.dynamicMapkey!!)
        placesClient = Places.createClient(mContext!!)
        bookingRideService = BookingRideService()
        bookingRideService?.BookingRideService(mContext!!)

    }


    // InitMaps & Current Locations Setup
    private var isCalledLocationChange = 0
    private fun initMaps() {
        if (mMap == null) {
            val fm = childFragmentManager
            mapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
        }

        if (mMap != null) {
            setupMap()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val style = MapStyleOptions.loadRawResourceStyle(mContext!!, R.raw.map_style)
        mMap?.setMapStyle(style)
        setupMap()

    }

    private fun setupMap() {
        Log.e("setupMap", "called")
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap?.isMyLocationEnabled = true
        mMap?.isBuildingsEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isMapToolbarEnabled = false
        mMap?.uiSettings?.isCompassEnabled = false
        mMap!!.setOnMyLocationChangeListener { location ->
            val myLocation = LatLng(location.latitude, location.longitude)

            if (myPreference.CurrentRequestId.isNullOrBlank()) {
                moveCamera(myLocation)
                animateCamera(myLocation)

            }

            current_lat = myLocation.latitude
            current_lng = myLocation.longitude


            isCalledLocationChange++
        }
    }

    private fun moveCamera(latLng: LatLng) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15.5f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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
    fun onMessageEvent(event: OnHomeLocationEnableModel) {
        if (event.isEnabled) {
            initMaps()
        }

    }

    private fun initCallbacks() {
        mylocationButton.setOnClickListener(this)
        sourceLayout.editHomeBottomSearchdest.setOnClickListener(this)
        sourceLayout.bottomHomeaddressLayout.setOnClickListener(this)
        sourceLayout.bottomWorkaddressLayout.setOnClickListener(this)
        servicetypeLayout.imgServicetypeback.setOnClickListener(this)
        servicetypeLayout.imgServicetypemyLocation.setOnClickListener(this)
        sourceLayout.layoutHomeAddress.txtHomeaddressClose.setOnClickListener(this)
        sourceLayout.bottomHomeaddressLayout.setOnClickListener(this)
        sourceLayout.layoutHomeAddress.homeAddressLayout.setOnClickListener(this)
        sourceLayout.layoutHomeAddress.workAddressLayout.setOnClickListener(this)
        sourceLayout.layoutHomeAddress.txtSetonmap.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        when (id) {
            R.id.cardview_mylocation_home -> {
                val latLng = LatLng(mMap?.myLocation?.latitude!!, mMap?.myLocation?.longitude!!)
                animateCamera(latLng)
            }
            R.id.edit_home_bottom_searchdest -> {
                bottom_sheet_homebehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                sourceLayout.layoutHomeDefault.visibility = View.GONE
                sourceLayout.cardviewMylocationHome.visibility = View.GONE
                sourceLayout.layoutHomeExpanded.visibility = View.VISIBLE
            }
            R.id.bottom_homeaddress_layout -> {

                if (locationHomeArray.size > 0) {
                    for (ida in locationHomeArray) {
                        Constant.DEST_LAT = ida.latitude
                        Constant.DEST_LNG = ida.longitude
                        Constant.DEST_ADDRESS = ida.address
                        Constant.DEST_TITLE = ida.title
                        getSourceAddressHome()
                        getServiceTypeView()
                    }
                } else {
                    MoveSetLocation(2001, "Home")
                }

            }
            R.id.home_address_layout -> {

                if (locationHomeArray.size > 0) {
                    for (ida in locationHomeArray) {
                        Constant.DEST_LAT = ida.latitude
                        Constant.DEST_LNG = ida.longitude
                        Constant.DEST_ADDRESS = ida.address
                        Constant.DEST_TITLE = ida.title
                        getSourceAddressHome()
                        getServiceTypeView()
                        val imm =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                } else {
                    MoveSetLocation(2001, "Home")
                }

            }
            R.id.bottom_workaddress_layout -> {
                if (locationWorkArray.size > 0) {
                    for (ida in locationWorkArray) {
                        Constant.DEST_LAT = ida.latitude
                        Constant.DEST_LNG = ida.longitude
                        Constant.DEST_ADDRESS = ida.address
                        Constant.DEST_TITLE = ida.title
                        getSourceAddressHome()
                        getServiceTypeView()
                    }
                } else {
                    MoveSetLocation(2002, "Work")
                }
            }
            R.id.work_address_layout -> {
                if (locationWorkArray.size > 0) {
                    for (ida in locationWorkArray) {
                        Constant.DEST_LAT = ida.latitude
                        Constant.DEST_LNG = ida.longitude
                        Constant.DEST_ADDRESS = ida.address
                        Constant.DEST_TITLE = ida.title
                        getSourceAddressHome()
                        getServiceTypeView()
                        val imm =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                } else {
                    MoveSetLocation(2002, "Work")
                }
            }
            R.id.img_servicetypeback -> {
                if (servicetypeLayout.layoutRootServicetype.visibility == View.VISIBLE) {
                    mMap?.clear()
                    servicetypeLayout.layoutRootServicetype.visibility = View.GONE
                    sourceLayout.coordinatorLayoutHome.visibility = View.VISIBLE
                    mapCollapsed()
                    EventBus.getDefault().postSticky(MenuIconDisableModel(false))
                    if (bottom_sheet_homebehavior != null) {
                        bottom_sheet_homebehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
            R.id.img_servicetypemyLocation -> {
                val latLng = LatLng(mMap?.myLocation?.latitude!!, mMap?.myLocation?.longitude!!)
                animateCamera(latLng)
            }
            R.id.txt_homeaddress_close -> {
                if (bottom_sheet_homebehavior != null) {
                    bottom_sheet_homebehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
            R.id.txt_setonmap -> {
                val bundle = Bundle()
                bundle.putInt("from", 2003)
                MyActivity.launchWithBundleResult(this, SetOnMapActivity::class.java, bundle, 2003)
                searchtyping = 1
            }

        }
    }

    private fun MoveSetLocation(i: Int, type: String) {

        val bundle = Bundle()
        bundle.putInt("from", i)
        bundle.putString("locationtype", type)
        bundle.putDouble("CurrentLat", current_lat!!)
        bundle.putDouble("CurrentLng", current_lng!!)
        MyActivity.launchWithBundleResult(this, SetLocationSearchActivity::class.java, bundle, i)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2001 || requestCode == 2002) {
            bookingRideService?.getSavedLocations(this)
        } else if (requestCode == 2003) {
            Constant.DEST_LAT = data?.getDoubleExtra("d_lat", 0.0)!!
            Constant.DEST_LNG = data.getDoubleExtra("d_lng", 0.0)!!
            Constant.DEST_ADDRESS = data.getStringExtra("daddress")!!
            Constant.DEST_TITLE = data.getStringExtra("dtitle")!!
            getSourceAddressHome()
            getServiceTypeView()
        }
    }

    //BottomSheetFunctions
    var coordinator_layout_home: CoordinatorLayout? = null
    var bottom_sheet_homebehavior: BottomSheetBehavior<*>? = null
    var searchtyping = 0
    var SearcyTypeSourceorDest = 0
    var predictions: PlacePredictions = PlacePredictions()

    @SuppressLint("SetTextI18n")
    private fun initBottomSheetAnimations() {

        coordinator_layout_home = sourceLayout.coordinatorLayoutHome
        val bottom_sheet_homenew =
            coordinator_layout_home!!.findViewById<View>(R.id.bottom_sheet_home)
        bottom_sheet_homebehavior = BottomSheetBehavior.from(bottom_sheet_homenew)
        bottom_sheet_homebehavior?.peekHeight = 740
        sourceLayout.txtWelcomeName.text = "Hello " + myPreference.firstName

        bottom_sheet_homebehavior?.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottom_sheet_homenew.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    sourceLayout.layoutHomeDefault.visibility = View.GONE
                    sourceLayout.cardviewMylocationHome.visibility = View.GONE
                    sourceLayout.layoutHomeExpanded.visibility = View.VISIBLE

                    sourceLayout.layoutHomeAddress.editSourceAddress.setText("")
                    sourceLayout.layoutHomeAddress.editDestAddress.setText("")
                    sourceLayout.layoutHomeAddress.editDestAddress.requestFocus()
                    val imm =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(
                        sourceLayout.layoutHomeAddress.editDestAddress,
                        InputMethodManager.SHOW_FORCED
                    )
                    EventBus.getDefault().postSticky(MenuIconDisableModel(true))
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    sourceLayout.layoutHomeDefault.visibility = View.VISIBLE
                    sourceLayout.cardviewMylocationHome.visibility = View.VISIBLE
                    sourceLayout.layoutHomeExpanded.visibility = View.GONE
                    EventBus.getDefault().postSticky(MenuIconDisableModel(false))
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sourceLayout.layoutHomeDefault.visibility = View.GONE
                    sourceLayout.cardviewMylocationHome.visibility = View.GONE
                    sourceLayout.layoutHomeExpanded.visibility = View.VISIBLE
                    EventBus.getDefault().postSticky(
                        MenuIconDisableModel(
                            true
                        )
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        sourceLayout.layoutHomeAddress.editDestAddress.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    KeyboardUtils.hideKeyboard(v)
                }
            }
        sourceLayout.layoutHomeAddress.editDestAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length == 0) {
                    searchtyping = 0
                }
            }

            override fun afterTextChanged(sValue: Editable) {
                if (sValue.toString().length > 0 && searchtyping == 0) {
                    SearcyTypeSourceorDest = 1
                    searchPlaces(sValue.toString())
                    sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.visibility =
                        View.VISIBLE
                    sourceLayout.layoutHomeAddress.searchResultView.visibility = View.GONE
                    sourceLayout.layoutHomeAddress.txtSetonmap.visibility = View.VISIBLE
                    sourceLayout.layoutHomeAddress.suggestions.text = "Suggestions"
                    sourceLayout.layoutHomeAddress.editDestAddress.setSelection(sValue.toString().length)
                } else {
                    sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.visibility = View.GONE
                    sourceLayout.layoutHomeAddress.searchResultView.visibility = View.VISIBLE
                    sourceLayout.layoutHomeAddress.suggestions.text = "Recent Locations"
                }
            }
        })
        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.onItemClickListener =
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
                    sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.visibility = View.GONE
                    Constant.DEST_LAT = place.latLng!!.latitude
                    Constant.DEST_LNG = place.latLng!!.longitude
                    Constant.DEST_ADDRESS = place.address!!
                    Constant.DEST_TITLE = place.name!!
                    getSourceAddressHome()
                    getServiceTypeView()
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        val apiException = exception
                        val statusCode = apiException.statusCode
                    }
                }
            }

        bookingRideService?.getSavedLocations(this)
    }

    // AutoComplete Adapter
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
                    predictions = gson.fromJson(response.toString(), PlacePredictions::class.java)
                    if (mAutoCompleteAdapter == null) {
                        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.visibility =
                            View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            mContext!!,
                            predictions.getPlaces()!!
                        )
                        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.adapter =
                            mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                    } else {
                        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.visibility =
                            View.VISIBLE
                        mAutoCompleteAdapter = AutoCompleteAdapter(
                            mContext!!,
                            predictions.getPlaces()!!
                        )
                        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.adapter =
                            mAutoCompleteAdapter
                        mAutoCompleteAdapter?.notifyDataSetChanged()
                        sourceLayout.layoutHomeAddress.listviewAutocompeleteHome.invalidate()
                    }
                }
            ) { error -> Log.v("SearchResponse", error.toString()) }
            AppController.applicationInstance.addToRequestQueue(jsonObjectRequest)
        }
    }

    private fun onGoingLayout() {
        sourceLayout.layoutHomeDefault.visibility = View.VISIBLE
        if (bottom_sheet_homebehavior != null) {
            bottom_sheet_homebehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    lateinit var locationHomeArray: ArrayList<GetLocationData>
    lateinit var locationWorkArray: ArrayList<GetLocationData>
    lateinit var locationrecentDestList: MutableList<RecentDestLocationData>

    @SuppressLint("SetTextI18n")
    override fun ongetSavedLocationsHome(getLocationResponse: GetLocationResponse) {
        Log.e("reasseow", "" + getLocationResponse.toString())
        locationHomeArray = arrayListOf()
        locationWorkArray = arrayListOf()

        if (getLocationResponse.locations != null) {
            for (locationdata in getLocationResponse.locations) {
                if (locationdata.type.equals("Home") && locationdata.is_default == 1) {
                    locationHomeArray.add(locationdata)

                }
                if (locationdata.type.equals("Work") && locationdata.is_default == 1) {
                    locationWorkArray.add(locationdata)

                }
            }
            for (locationdata in locationHomeArray) {
                sourceLayout.txtHomeAddressHome.text = locationdata.title
                sourceLayout.layoutHomeAddress.txtDialogHomeAddress.text = locationdata.title
            }
            for (locationdata in locationWorkArray) {
                sourceLayout.txtWorkAddressHome.text = locationdata.title
                sourceLayout.layoutHomeAddress.txtDialogWorkAddress.text = locationdata.title
            }
        }
        initRecentDestionLocationList(getLocationResponse.recent_destination)
    }

    private fun initRecentDestionLocationList(recentDestination: MutableList<RecentDestLocationData>?) {
        if (recentDestination!!.size > 0) {
            sourceLayout.layoutHomeAddress.searchResultView.visibility = View.VISIBLE
            sourceLayout.layoutHomeAddress.txtEmptyLocationsFound.visibility = View.GONE
            locationrecentDestList = mutableListOf()
            locationrecentDestList = recentDestination.take(5).toMutableList()
            val historyAdapter = RecentDestLocationAdapter(
                mContext!!,
                recentDestination.take(5).toMutableList(),
                this
            )
            sourceLayout.layoutHomeAddress.searchResultView.layoutManager =
                LinearLayoutManager(mContext!!)
            sourceLayout.layoutHomeAddress.searchResultView.adapter = historyAdapter

        } else {
            sourceLayout.layoutHomeAddress.searchResultView.visibility = View.GONE
            sourceLayout.layoutHomeAddress.txtEmptyLocationsFound.visibility = View.VISIBLE
        }

    }


    override fun onRecentDestItemClick(position: Int) {
        getSourceAddressHome()
        val recentlist = locationrecentDestList.get(position)
        Constant.DEST_LAT = recentlist.d_latitude!!
        Constant.DEST_LNG = recentlist.d_longitude!!
        Constant.DEST_ADDRESS = recentlist.d_address!!
        Constant.DEST_TITLE = recentlist.d_title!!
        getServiceTypeView()

    }

    private fun getSourceAddressHome() {
        if (current_lat != null && current_lng != null) {
            Constant.SOURCE_LAT = current_lat!!
            Constant.SOURCE_LNG = current_lng!!
            if (sourceLayout.layoutHomeAddress.editSourceAddress.text.toString().isEmpty()) {
                servicetypeLayout.txtLableSource.text = "Current Location"
            } else {
                servicetypeLayout.txtLableSource.text =
                    sourceLayout.layoutHomeAddress.editSourceAddress.text.toString()
            }
        } else {
            ToastBuilder.build(mContext!!, "Location Detecting.. Please wait")
        }


    }

    // Create Service Type Screen
    private fun getServiceTypeView() {
        sourceLayout.coordinatorLayoutHome.visibility = View.GONE
        sourceLayout.cardviewMylocationHome.visibility = View.GONE
        servicetypeLayout.layoutRootServicetype.visibility = View.VISIBLE
        servicetypeLayout.txtLableDestination.text = Constant.DEST_TITLE
        EventBus.getDefault().postSticky(MenuIconDisableModel(true))
        generateRoute()
    }


    // Route Generation
    var sourceMarker: Drawable? = null
    var destinationMarker: Drawable? = null

    open fun generateRoute() {
        sourceMarker = mContext!!.resources.getDrawable(R.drawable.img_source_address)
        destinationMarker = mContext!!.resources.getDrawable(R.drawable.img_destination_address)

        try {
            val s_lat: Double = Constant.SOURCE_LAT
            val s_lng: Double = Constant.SOURCE_LNG
            val d_lat: Double = Constant.DEST_LAT
            val d_lng: Double = Constant.DEST_LNG
            val start = LatLng(s_lat, s_lng)
            val end = LatLng(d_lat, d_lng)
            if (s_lat > 0 && s_lng > 0 && d_lat > 0 && d_lng > 0) {
                val myLocation = LatLng(start.latitude, start.longitude)
                val cameraPosition = CameraPosition.Builder().target(myLocation).zoom(12f).build()
                mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                val route: MutableList<LatLng> = ArrayList()
                route.add(LatLng(s_lat, s_lng))
                //   zoomRoute(mMap, route);
            }
            val routing: Routing = Routing.Builder()
                .key(myPreference.dynamicMapkey)
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .alternativeRoutes(false)
                .withListener(this)
                .waypoints(start, end)
                .build()
            routing.execute()
        } catch (e: Exception) {
        }
    }


    override fun onRoutingFailure(p0: RouteException?) {
        Log.e("onRoutingFailure: ", "" + p0?.message)
    }

    override fun onRoutingStart() {
    }

    private var polylines: ArrayList<Polyline> = arrayListOf()
    private var polylinesValues: ArrayList<LatLng>? = null
    private var greyPolyLine: Polyline? = null
    private var blackPolyline: Polyline? = null

    override fun onRoutingSuccess(route: ArrayList<Route>?, p1: Int) {
        mMap?.clear()

        val start = LatLng(Constant.SOURCE_LAT, Constant.SOURCE_LNG)
        val end = LatLng(Constant.DEST_LAT, Constant.DEST_LNG)
        val builder = LatLngBounds.Builder()
        //the include method will calculate the min and max bound.
        builder.include(start)
        builder.include(end)
        val bounds = builder.build()
        if (getDistance(start.latitude, start.longitude, end.latitude, end.longitude) < 2) {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 100)
            mMap!!.animateCamera(cu)
            Log.e("getDistancelocal<2:", "Success")
            mapExpand()
        } else if (getDistance(
                start.latitude,
                start.longitude,
                end.latitude,
                end.longitude
            ) > 2 && getDistance(start.latitude, start.longitude, end.latitude, end.longitude) < 5
        ) {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 150)
            mMap!!.animateCamera(cu)
            mapExpand()
            Log.e("getDistancelocal>2<5:", "Success")
        } else if (getDistance(
                start.latitude,
                start.longitude,
                end.latitude,
                end.longitude
            ) > 5 && getDistance(start.latitude, start.longitude, end.latitude, end.longitude) < 10
        ) {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
            mMap!!.animateCamera(cu)
            mapExpand()
            Log.e("getDistancelocal>5<10:", "Success")
        } else if (getDistance(
                start.latitude,
                start.longitude,
                end.latitude,
                end.longitude
            ) > 10 && getDistance(start.latitude, start.longitude, end.latitude, end.longitude) < 15
        ) {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 250)
            mMap!!.animateCamera(cu)
            mapExpand()
            Log.e("getDistancelocal>10<15:", "Success")
        } else {
            Log.e("getDistancelocal>15:", "Success")
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 300)
            mMap!!.animateCamera(cu)
            mapExpand()
        }
        if (polylines.size > 0) {
            for (poly in polylines) {
                poly.remove()
            }
        }

        polylines = arrayListOf()
        polylinesValues = arrayListOf()
        for (i in route!!.indices) {
            val polyOptions = PolylineOptions()
            polyOptions.color(Color.GRAY)
            polyOptions.width(6f)
            polyOptions.addAll(route.get(i).points)
            greyPolyLine = mMap!!.addPolyline(polyOptions)
            polylines.add(greyPolyLine!!)
            polylinesValues?.addAll(route.get(i).points)
            val blackPolylineOptions = PolylineOptions()
            blackPolylineOptions.width(6f)
            blackPolylineOptions.color(Color.BLACK)
            blackPolyline = mMap!!.addPolyline(blackPolylineOptions)
        }
        if (myPreference.CurrentRequestId.isNullOrEmpty()
        ) {
            // sharedHelper.setLocationRoute(polylinesValues)
        }
        val polylineAnimator: ValueAnimator = Utils.polyLineAnimator()!!
        polylineAnimator.addUpdateListener { animation ->
            val percentValue = animation.animatedValue as Int
            val index: Int = (greyPolyLine?.points!!.size * (percentValue / 100.0f)).toInt()
            blackPolyline?.points = greyPolyLine?.points!!.subList(0, index)
        }
        polylineAnimator.start()

        val height = 70
        val width = 70

        //source marker

        //source marker
        val bitmapdraw = sourceMarker as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)


        //desti_marker


        //desti_marker
        val destiIcon = destinationMarker as BitmapDrawable
        val bDest = destiIcon.bitmap
        val smallMarkerDest = Bitmap.createScaledBitmap(bDest, width, height, false)
        // Start marker
        // Start marker
        var options = MarkerOptions()
        if (myPreference.CurrentRequestId.isNullOrEmpty()) {
            options.position(start)
            options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            mMap!!.addMarker(options)
        }
        // End marker
        // End marker
        options = MarkerOptions()
        options.position(end)
        options.icon(BitmapDescriptorFactory.fromBitmap(smallMarkerDest))
        mMap!!.addMarker(options)
    }

    override fun onRoutingCancelled() {
    }

    private fun mapExpand() {
        if (mapFragment != null) {
            val displaymetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
            val height = displaymetrics.heightPixels
            val params = mapFragment!!.requireView().layoutParams
            params.height = height / 2 + 350
            mapFragment!!.requireView().layoutParams = params
        }
    }

    private fun mapCollapsed() {
        val displaymetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        if (mapFragment != null) {
            val params = mapFragment!!.requireView().layoutParams
            params.height = height
            mapFragment!!.requireView().layoutParams = params
            if (current_lat != null && current_lng != null) {
                val curlat = LatLng(current_lat!!.toDouble(), current_lng!!.toDouble())
                val cameraPosition = CameraPosition.Builder().target(curlat).zoom(15f).build()
                mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    fun getDistance(s_lat: Double, s_lng: Double, d_lat: Double, d_lng: Double): Double {
        val startPoint = Location("locationA")
        startPoint.latitude = s_lat
        startPoint.longitude = s_lng
        val endPoint = Location("locationA")
        endPoint.latitude = d_lat
        endPoint.longitude = d_lng
        return startPoint.distanceTo(endPoint).toDouble() / 1000
    }


    // OnBackPressHandler

    private fun backpressHandler() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (servicetypeLayout.layoutRootServicetype.visibility == View.VISIBLE) {
                        mMap?.clear()
                        servicetypeLayout.layoutRootServicetype.visibility = View.GONE
                        sourceLayout.coordinatorLayoutHome.visibility = View.VISIBLE
                        mapCollapsed()
                        EventBus.getDefault().postSticky(MenuIconDisableModel(false))

                    }
                    if (sourceLayout.coordinatorLayoutHome.visibility == View.VISIBLE) {
                        EventBus.getDefault().postSticky(MenuIconDisableModel(false))
                        mapCollapsed()
                    }
                    if (bottom_sheet_homebehavior != null) {
                        if (bottom_sheet_homebehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                            bottom_sheet_homebehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

                        }
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })

    }


}
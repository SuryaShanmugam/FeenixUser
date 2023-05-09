package com.app.feenix.view.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.app.feenix.R
import com.app.feenix.app.MyPreference
import com.app.feenix.databinding.FragmentHomeBinding
import com.app.feenix.eventbus.OnHomeLocationEnableModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : Fragment(), OnMapReadyCallback {
    private var mContext: Context? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var myPreference: MyPreference
    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initObjects()
        initMaps()
        return binding.root
    }


    private fun initObjects() {
        mContext = activity
        myPreference = MyPreference(mContext!!)
    }

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

    private var isCalledLocationChange = 0
    private fun setupMap() {

        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION
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
            if (isCalledLocationChange == 0) {
                val myLocation = LatLng(location.latitude, location.longitude)
                if (myPreference.CurrentRequestId.isNullOrBlank()) {
                    animateCamera(myLocation)
                }
            }

            isCalledLocationChange++
        }
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
}
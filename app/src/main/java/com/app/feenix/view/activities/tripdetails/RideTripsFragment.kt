package com.app.feenix.view.activities.tripdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.feenix.databinding.FragmentRidetripsBinding
import com.app.feenix.databinding.FragmentWalkthroughBinding

class RideTripsFragment: Fragment() {

    private var mContext: Context? = null
    private lateinit var binding: FragmentRidetripsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRidetripsBinding.inflate(inflater, container, false)
        initObjects()
        return binding.root
    }

    private fun initObjects() {


    }

}
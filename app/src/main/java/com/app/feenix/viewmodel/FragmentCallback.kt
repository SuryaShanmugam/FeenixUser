package com.app.feenix.viewmodel

import androidx.fragment.app.Fragment


interface FragmentCallback {

    fun launchFragment(fragment: Fragment, addToBackStack: Boolean)

}
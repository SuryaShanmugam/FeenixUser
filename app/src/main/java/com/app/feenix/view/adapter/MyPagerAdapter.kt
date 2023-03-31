package com.app.feenix.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPagerAdapter(private val fm: FragmentManager?, private val fragmentList: List<Fragment>?) :
    FragmentStatePagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }


}
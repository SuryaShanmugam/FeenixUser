package com.app.feenix.view.activities.base

import androidx.fragment.app.Fragment

/**
 * This class defines common functionality and is responsible for handling common logic related to activities.
 * BaseFragment should be the parent of all the fragment in the project.
 */
open class BaseFragment : Fragment() {

    fun hasInternetConnection(): Boolean {
        val baseActivity = activity as BaseActivity?
        baseActivity ?: return false
        return baseActivity.hasInternetConnection()
    }

}
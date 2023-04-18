package com.app.feenix.view.activities.Walkthrough

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.feenix.databinding.FragmentWalkthroughBinding

class WalkthroughFragment : Fragment() {
    private val mRootView: View? = null
    private var mContext: Context? = null
    private lateinit var binding: FragmentWalkthroughBinding


    companion object {
        fun newInstance(
            title: String?,
            desc: String?,
            mdesc1: String?,
            imgae: Int
        ): WalkthroughFragment {
            val fragment = WalkthroughFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("desc", desc)
            bundle.putString("desc1", mdesc1)
            bundle.putInt("image", imgae)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalkthroughBinding.inflate(inflater, container, false)
        initObjects()
        setIntro()
        return binding.root
    }


    private fun initObjects() {
        mContext = activity
    }

    private fun setIntro() {
        val bundle = arguments
        if (bundle != null) {
            binding.txtTitle.text = bundle.getString("title")
            binding.txtDesc.text = bundle.getString("desc")
            binding.imageview.setImageResource(bundle.getInt("image"))
        }
    }

}
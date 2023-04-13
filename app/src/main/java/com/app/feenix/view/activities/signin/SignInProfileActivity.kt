package com.app.feenix.view.activities.signin

import android.content.Context
import android.os.Bundle
import android.view.View
import com.app.feenix.databinding.ActivitySignInProfileBinding
import com.app.feenix.view.activities.base.BaseActivity

class SignInProfileActivity : BaseActivity() , View.OnClickListener {
    private lateinit var binding: ActivitySignInProfileBinding
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(v: View?) {
        val id= v?.id
        when (id)
        {

        }

    }
}
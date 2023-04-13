package com.hellotirupathur.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import com.app.feenix.R
import java.util.*

class TextChangedListener {
    companion object {
        fun onTextChanged(editTextSource: EditText,imagebutton:ImageView) {
            editTextSource.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.length > 0) {
                        imagebutton.setImageResource(R.drawable.ic_login_next_selected)
                        editTextSource.setBackgroundResource(R.drawable.bg_edittext_selected)
                    } else {
                        imagebutton.setImageResource(R.drawable.ic_login_next_unselected)
                        editTextSource.setBackgroundResource(R.drawable.bg_edittext_unselected)
                    }
                }
            })
        }



    }
}
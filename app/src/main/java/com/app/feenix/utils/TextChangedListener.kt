package com.hellotirupathur.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.feenix.R

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


        fun onTextPromocodesChanged(
            mEditPromocodes: EditText, context: Context,
            mApplyCode: Button, mTextPromoError: TextView
        ) {
            mEditPromocodes.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.toString().length > 0) {
                        mEditPromocodes.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.slide_text_color
                            )
                        )
                        mEditPromocodes.setBackgroundResource(R.drawable.bg_edittext_selected)
                        mApplyCode.visibility = View.VISIBLE
                        mTextPromoError.visibility = View.GONE
                    } else {
                        mApplyCode.visibility = View.GONE
                    }
                }
            })
        }

    }
}
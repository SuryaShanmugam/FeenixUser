package com.hellotirupathur.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
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

        fun onTextPriceEstimationPromocodeChanged(editTextSource: EditText, imagebutton: Button) {
            editTextSource.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.length > 0) {
                        imagebutton.visibility = View.VISIBLE
                    } else {
                        imagebutton.visibility = View.GONE
                    }
                }
            })
        }

        fun onCheckboxChangedListner(checkBox: CheckBox,editothercommends:EditText,
                                     checkBox1: CheckBox,
                                     checkBox2: CheckBox,
                                     checkBox3: CheckBox,
                                     checkBox4: CheckBox,
                                     checkBox5: CheckBox):String
        {
            var textratingcomments:String=""
            checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                editothercommends.visibility = View.GONE
                if (isChecked) {
                    checkBox1.isChecked = false
                    checkBox2.isChecked = false
                    checkBox3.isChecked = false
                    checkBox4.isChecked = false
                    checkBox5.isChecked = false
                    textratingcomments= checkBox.text.toString()

                } else {
                    textratingcomments = ""
                }
            })
            return textratingcomments
        }
    }



}
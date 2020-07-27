package com.hellotirupathur.utils

import android.widget.EditText
import android.widget.TextView
import java.util.*

class Validator {
    companion object {


        fun isValidationEditext(editText: EditText, Message: String): Boolean {

            if (editText.text.isBlank()) {
                editText.requestFocus()
                editText.error = String.format(Locale.getDefault(), Message, editText.hint)
                return false
            }
            return true
        }

        fun isValidationTextView(textView: TextView, Message: String): Boolean {
            if (textView.text.isBlank()) {
                textView.requestFocus()
                textView.error = String.format(Locale.getDefault(), Message, textView.hint)
                return false
            }
            return true
        }

    }
}
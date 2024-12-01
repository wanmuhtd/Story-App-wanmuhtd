package com.dicoding.wanmuhtd.storyapp.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.dicoding.wanmuhtd.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class CustomEmailEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val view = rootView
                val textInputLayout = view.findViewById<TextInputLayout>(R.id.tl_register_email)
                validateEmail(s.toString(), textInputLayout)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validateEmail(email: String, textInputLayout: TextInputLayout?) {
        val emailPattern = Pattern.compile(
            context.getString(R.string.regex_email)
        )
        val isValid = emailPattern.matcher(email).matches()

        if (textInputLayout != null) {
            if (isValid) {
                textInputLayout.error = null // Clear error if valid
            } else {
                textInputLayout.error = context.getString(R.string.error_invalid_email)
            }
        }
    }
}
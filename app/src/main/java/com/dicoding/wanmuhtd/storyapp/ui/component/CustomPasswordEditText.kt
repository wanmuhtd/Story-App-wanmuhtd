package com.dicoding.wanmuhtd.storyapp.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.dicoding.wanmuhtd.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomPasswordEditText : TextInputEditText {
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
                val textInputLayout = view.findViewById<TextInputLayout>(R.id.tl_register_password)
                if (s.toString().length < 8) {
                    textInputLayout?.error =
                        resources.getString(R.string.password_minimum_characters)
                } else {
                    textInputLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}
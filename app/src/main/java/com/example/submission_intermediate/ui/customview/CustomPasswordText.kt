package com.example.submission_intermediate.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submission_intermediate.R

class CustomPasswordText  : AppCompatEditText  {

    private var isPasswordVisible: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textSize = 15f
        hint = "Password"
        gravity = Gravity.START
        context.apply {
            background = ContextCompat.getDrawable(this, R.drawable.custom_input_text)
            setTextColor(ContextCompat.getColor(this, R.color.primaryKey))
            setHintTextColor(ContextCompat.getColor(this, R.color.secondaryKey))
        }
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                error = null
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.isNotEmpty()) {
                    if (s.length < 8) {
                        context.getString(R.string.password_minchar)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

}

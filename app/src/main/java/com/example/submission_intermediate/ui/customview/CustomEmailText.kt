package com.example.submission_intermediate.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submission_intermediate.R
import java.util.regex.Pattern

class CustomEmailText : AppCompatEditText{

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

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
        hint = "Email"
        gravity = Gravity.LEFT
        context.apply {
            background = ContextCompat.getDrawable(this, R.drawable.custom_input_text)
            setTextColor(ContextCompat.getColor(this, R.color.primaryKey))
            setHintTextColor(ContextCompat.getColor(this, R.color.secondaryKey))
        }
        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.isNotEmpty()) {
                    if (!isValidEmail(s.toString())) {
                        "Invalid email format"
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

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }

    fun setErrorMessage(errorMessage: String) {
        error = errorMessage
    }
}
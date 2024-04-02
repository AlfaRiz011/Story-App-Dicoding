package com.example.submission_intermediate.uitls

import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.widget.Button
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

object CustomViewHelper {

    fun loginHelper(emailEd: EditText, passEd: EditText, button: Button) {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonEnable(emailEd, passEd, button)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        emailEd.addTextChangedListener(watcher)
        passEd.addTextChangedListener(watcher)
    }

    fun registHelper(nameEd: EditText,emailEd: EditText, passEd: EditText, button: Button) {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonEnable(nameEd,emailEd, passEd, button)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        nameEd.addTextChangedListener(watcher)
        emailEd.addTextChangedListener(watcher)
        passEd.addTextChangedListener(watcher)
    }

    private fun setButtonEnable(nameEd: EditText, emailEd: EditText, passEd: EditText, button: Button) {
        val resultName = nameEd.text.toString()
        val resultEmail = emailEd.text.toString()
        val resultPass = passEd.text.toString()

        val isValidEmail = isValidEmail(resultEmail)
        val isPasswordValid = resultPass.length >= 8

        button.isEnabled = isValidEmail && isPasswordValid && resultEmail.isNotEmpty() && resultPass.isNotEmpty() && resultName.isNotEmpty()
    }

    private fun setButtonEnable(emailEd: EditText, passEd: EditText, button: Button) {
        val resultEmail = emailEd.text.toString()
        val resultPass = passEd.text.toString()

        val isValidEmail = isValidEmail(resultEmail)
        val isPasswordValid = resultPass.length >= 8

        button.isEnabled = isValidEmail && isPasswordValid && resultEmail.isNotEmpty() && resultPass.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }


}
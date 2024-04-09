package com.example.submission_intermediate.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import android.provider.Settings
import android.view.WindowManager
import com.example.submission_intermediate.databinding.ActivityUserBinding
import com.example.submission_intermediate.ui.auth.AuthActivity
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.uitls.UserPreferences

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var userId: String
    private lateinit var username: String
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        preferences = UserPreferences.getInstance(this.dataStore)
        userViewModel = ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userId = ""
        username = ""

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        with(binding){
            btnLanguage.setOnClickListener { changeLanguage() }
            logout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        userViewModel.clearDataLogin()
        val intent = Intent(this@UserActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun changeLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun setupViewModel() {
        userViewModel.getUid().observe(this){
            userId = it
            updateView()
        }

        userViewModel.getName().observe(this){
            username = it
            updateView()
        }
    }

    private fun setupView() {
        with(binding){
            uid.text = userId
            name.text = username
        }
    }

    private fun updateView() {
        with(binding){
            uid.text = userId
            name.text = username
        }
    }
}

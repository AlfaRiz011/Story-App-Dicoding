package com.example.submission_intermediate.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.submission_intermediate.databinding.ActivitySplashBinding
import com.example.submission_intermediate.ui.MainActivity
import com.example.submission_intermediate.ui.auth.AuthActivity
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.UserPreferences

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val preferences = UserPreferences.getInstance(this.dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getLoginSession().observe(this) { isSessionAvailable ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (isSessionAvailable) {
                    navigateToMainActivity()
                } else {
                    navigateToAuthActivity()
                }
            }, 2000)
        }
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

package com.example.submission_intermediate.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.ActivityMainBinding
import com.example.submission_intermediate.ui.home.HomeFragment
import com.example.submission_intermediate.ui.story.UploadActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setView()
    }

    private fun setView() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.nav_view)

        bottomNav.setupWithNavController(navController)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
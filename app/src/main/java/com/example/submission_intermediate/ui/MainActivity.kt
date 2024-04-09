package com.example.submission_intermediate.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.ActivityMainBinding
import com.example.submission_intermediate.ui.home.HomeFragment
import com.example.submission_intermediate.ui.story.UploadActivity


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

        supportFragmentManager
            .beginTransaction()
            .add(R.id.coordinator, HomeFragment())
            .commit()

        setAction()
    }

    private fun setAction() {

        binding.addStory.setOnClickListener{
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
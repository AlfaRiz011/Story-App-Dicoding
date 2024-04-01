package com.example.submission_intermediate.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.submission_intermediate.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
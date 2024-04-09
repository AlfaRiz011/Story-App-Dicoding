package com.example.submission_intermediate.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submission_intermediate.databinding.ActivityDetailBinding
import com.example.submission_intermediate.service.response.DetailResponse
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.UserPreferences

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var token: String
    private lateinit var storyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val storyID = intent.getStringExtra(UID)
        if (storyID != null) {
            storyId = storyID
        } else {
            showToast("StoryId Kosong")
        }

        setViewModel()
    }
    
    private fun setViewModel(){
        val preferences = UserPreferences.getInstance(this.dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getToken().observe(this){
            token = it
            detailViewModel.getStoryDetail(token,storyId)
        }

        detailViewModel.message.observe(this){message ->
            detailRes(
                detailViewModel.isError,
                message
            )
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailViewModel.getStoriesData().observe(this){stories ->
            setView(stories)
        }
    }

    private fun setView(stories: DetailResponse) {
        with(binding){
            storyName.text = stories.story.name
            storyDescription.text = stories.story.description
            storyImage.loadImage(url = stories.story.photoUrl)
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }

    private fun detailRes(error: Boolean, message: String) {
        if(error) {
            showToast("Gagal mengambil data")
            showToast(message)
        }
    }

    private fun showToast(msg: String) {
    Toast.makeText(
        this@DetailActivity,
        msg,
        Toast.LENGTH_LONG).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val UID = "extra_id"
    }
}
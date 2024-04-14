package com.example.submission_intermediate.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission_intermediate.database.StoryDatabase
import com.example.submission_intermediate.service.api.ApiService

class HomeViewModelFactory (val context: Context, private val apiService: ApiService, val token:String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val database = StoryDatabase.getDatabase(context)
            return HomeViewModel(
                com.example.submission_intermediate.data.StoryRepository(
                    database,
                    apiService, token
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
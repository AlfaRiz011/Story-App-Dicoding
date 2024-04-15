package com.example.submission_intermediate.di

import android.content.Context
import com.example.submission_intermediate.data.StoryRepository
import com.example.submission_intermediate.database.StoryDatabase
import com.example.submission_intermediate.service.api.ApiConfig

object Injection {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, token)
    }
}
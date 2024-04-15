package com.example.submission_intermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submission_intermediate.database.StoryDatabase
import com.example.submission_intermediate.service.api.ApiService
import com.example.submission_intermediate.service.response.StoryDB

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService, private val token: String) {
    fun getStory(): LiveData<PagingData<StoryDB>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}
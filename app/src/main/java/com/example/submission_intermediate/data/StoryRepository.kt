package com.example.submission_intermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.submission_intermediate.database.StoryDB
import com.example.submission_intermediate.database.StoryDatabase
import com.example.submission_intermediate.service.api.ApiService

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token:String
) {
    fun getStory(): LiveData<PagingData<StoryDB>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
            pagingSourceFactory = { storyDatabase.storyDao().getAllStory() }
        ).liveData
    }
}
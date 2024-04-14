package com.example.submission_intermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submission_intermediate.data.StoryRepository
import com.example.submission_intermediate.database.StoryDB

class HomeViewModel (storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<StoryDB>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}
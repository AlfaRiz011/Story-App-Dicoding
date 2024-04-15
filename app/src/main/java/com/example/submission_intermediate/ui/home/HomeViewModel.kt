package com.example.submission_intermediate.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import com.example.submission_intermediate.data.StoryRepository
import com.example.submission_intermediate.di.Injection
import com.example.submission_intermediate.service.response.StoryDB

class HomeViewModel(storyRepository: StoryRepository)  : ViewModel() {

    val story: LiveData<PagingData<StoryDB>> =
        storyRepository.getStory().cachedIn(viewModelScope)

}

class MainViewModelFactory(private val context: Context, private val token: String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideRepository(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.submission_intermediate.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission_intermediate.service.api.ApiConfig
import com.example.submission_intermediate.service.response.StoriesResponse
import retrofit2.Call
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val _stories = MutableLiveData<StoriesResponse>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false

    fun getStoryWithLocation(token: String){
        _isLoading.value = true
        val api = ApiConfig.getApiService().getStoryWithLoc("Bearer $token", 100)
        api.enqueue(object : retrofit2.Callback<StoriesResponse> {
            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _stories.value = response.body()
                    }
                } else {
                    isError = true
                    _message.value =  "${response.code()} || ${response.message()}"
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message ?: ""
            }
        })
    }

    fun getStoriesWithLocData(): LiveData<StoriesResponse> {
        return _stories
    }
}
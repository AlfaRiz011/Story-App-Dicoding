package com.example.submission_intermediate.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission_intermediate.service.api.ApiConfig
import com.example.submission_intermediate.service.response.DetailResponse
import retrofit2.Call
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _stories = MutableLiveData<DetailResponse>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false

    fun getStoryDetail(token: String, storyId: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getStoryById("Bearer $token", storyId)
        api.enqueue(object : retrofit2.Callback<DetailResponse>{
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _stories.value = response.body()
                    }
                    _message.value = responseBody?.message ?: ""

                } else {
                    isError = true
                    _message.value =  "${response.code()} || ${response.message()}"
                }
            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message ?: ""
            }
        })
    }

    fun getStoriesData(): LiveData<DetailResponse> {
        return _stories
    }
}
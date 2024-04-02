package com.example.submission_intermediate.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission_intermediate.service.api.ApiConfig
import com.example.submission_intermediate.service.response.StoryAddResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(img: MultipartBody.Part, description: RequestBody, token: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().uploadStory(img, description, "Bearer $token")
        api.enqueue(object : Callback<StoryAddResponse> {
            override fun onResponse(
                call: Call<StoryAddResponse>,
                response: Response<StoryAddResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        _message.value = responseBody.message ?: ""
                    } else {
                        _message.value = "Error: ${response.message()}"
                    }
                } else {
                    _message.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<StoryAddResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Error: ${t.message}"
            }
        })
    }
}

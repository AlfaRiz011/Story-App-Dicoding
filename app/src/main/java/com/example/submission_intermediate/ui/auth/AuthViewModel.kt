package com.example.submission_intermediate.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission_intermediate.service.api.ApiConfig
import com.example.submission_intermediate.service.response.LoginData
import com.example.submission_intermediate.service.response.LoginResponse
import com.example.submission_intermediate.service.response.RegisterData
import com.example.submission_intermediate.service.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult
    private val _messageLogin = MutableLiveData<String>()
    val messageLogin: LiveData<String> = _messageLogin
    var isLogin = false

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult
    private val _messageRegister = MutableLiveData<String>()
    val messageRegister: LiveData<String> = _messageRegister
    var isRegistered = false

    val loginData = MutableLiveData<LoginData>()

    fun getLoginResponse(loginData: LoginData){
        _isLoading.value = true
        val client = ApiConfig.getApiService().doLogin(loginData)
        client.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        isLogin = true
                        _loginResult.value = response.body()
                        _messageLogin.value = response.message()
                    }
                } else {
                    isLogin = false
                    when (response.code()) {
                        401 -> _messageLogin.value =
                            "Email atau password yang anda masukan salah, silahkan coba lagi"
                        408 -> _messageLogin.value =
                            "Koneksi internet bermasalah, silahkan coba lagi"
                        else -> _messageLogin.value = "Pesan error: " + response.message()
                    }
                    _isLoading.value = false

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLogin = false
                _isLoading.value = false
                _messageLogin.value = "Pesan Failure: " + t.message.toString()
            }

        })
    }

    fun getRegisterResponse(registerData: RegisterData) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().doRegister(registerData)
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        isRegistered = true
                        _registerResult.value = response.body()
                        _messageRegister.value = response.message()
                    }
                } else {
                    isRegistered = false
                    when (response.code()) {
                        400 -> _messageRegister.value =
                            "Email sudah digunakan, coba dengan email yang lain"
                        408 -> _messageRegister.value =
                            "Koneksi internet bermasalah, silahkan coba lagi"
                        else -> _messageRegister.value = "Pesan error: " + response.message()
                    }
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                isRegistered = false
                _isLoading.value = false
                _messageRegister.value = "Pesan Failure: " + t.message.toString()
            }

        })
    }

    fun setLoginData(email: String, password: String) {
        loginData.value = LoginData(email = email, password = password)
    }


}
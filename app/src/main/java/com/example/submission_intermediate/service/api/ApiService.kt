package com.example.submission_intermediate.service.api

import com.example.submission_intermediate.service.response.DetailResponse
import com.example.submission_intermediate.service.response.LoginData
import com.example.submission_intermediate.service.response.LoginResponse
import com.example.submission_intermediate.service.response.RegisterResponse
import com.example.submission_intermediate.service.response.RegisterData
import com.example.submission_intermediate.service.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    fun doLogin(
        @Body requestLogin: LoginData
    ): Call<LoginResponse>

    @POST("register")
    fun doRegister(
        @Body requestRegister: RegisterData
    ): Call<RegisterResponse>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") storyId: String
    ): Call<DetailResponse>

}

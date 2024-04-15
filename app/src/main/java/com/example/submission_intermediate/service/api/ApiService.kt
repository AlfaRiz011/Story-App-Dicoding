package com.example.submission_intermediate.service.api

import com.example.submission_intermediate.service.response.DetailResponse
import com.example.submission_intermediate.service.response.LoginData
import com.example.submission_intermediate.service.response.LoginResponse
import com.example.submission_intermediate.service.response.RegisterResponse
import com.example.submission_intermediate.service.response.RegisterData
import com.example.submission_intermediate.service.response.StoriesResponse
import com.example.submission_intermediate.service.response.StoryAddResponse
import com.example.submission_intermediate.service.response.StoryDB
import com.example.submission_intermediate.service.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories?location=1")
    fun getStoryWithLoc(
        @Header("Authorization") token: String,
        @Query("size") size : Int
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") storyId: String
    ): Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<StoryAddResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
    ): Call<StoryAddResponse>


}

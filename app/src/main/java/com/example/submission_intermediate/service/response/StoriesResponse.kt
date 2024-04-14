package com.example.submission_intermediate.service.response

import com.example.submission_intermediate.database.StoryDB
import com.google.gson.annotations.SerializedName

data class StoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<StoryDB>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

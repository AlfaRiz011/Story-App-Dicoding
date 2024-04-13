package com.example.submission_intermediate.service.response

import com.google.gson.annotations.SerializedName

data class StoryAddResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class StoryData(
	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("lon")
	val lon: String? = null,
)

package com.dicoding.wanmuhtd.storyapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)


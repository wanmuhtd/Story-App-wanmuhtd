package com.dicoding.wanmuhtd.storyapp.data.remote.model

import com.dicoding.wanmuhtd.storyapp.data.local.entity.Story
import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<Story> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
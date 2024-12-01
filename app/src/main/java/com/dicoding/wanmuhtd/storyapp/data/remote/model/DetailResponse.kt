package com.dicoding.wanmuhtd.storyapp.data.remote.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)

@Entity(tableName = "story")
data class Story(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Float,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Float
)
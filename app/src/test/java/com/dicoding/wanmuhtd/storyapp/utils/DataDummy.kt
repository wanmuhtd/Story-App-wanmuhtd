package com.dicoding.wanmuhtd.storyapp.utils

import com.dicoding.wanmuhtd.storyapp.data.local.entity.Story
import com.dicoding.wanmuhtd.storyapp.data.remote.model.LoginResponse
import com.dicoding.wanmuhtd.storyapp.data.remote.model.LoginResult

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val listStory = ArrayList<Story>()
        for (i in 1..20) {
            val story = Story(
                createdAt = "2024-12-16T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                lat = (i * 10).toFloat(),
                lon = (i * 10).toFloat(),
                name = "Name $i",
                photoUrl = "https://media.licdn.com/dms/image/v2/D5616AQExQvqURpTvKg/profile-displaybackgroundimage-shrink_350_1400/profile-displaybackgroundimage-shrink_350_1400/0/1719353299058?e=1740009600&v=beta&t=2LumDfDpZu4-Iab5hEmNcLIyplLTfnQBJ0xPmBgMBeE"
            )
            listStory.add(story)
        }
        return listStory
    }

    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                name = "Arwan Muhtada",
                userId = "C7I5P6ZO7bPZ7K_A",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUM3STVQNlpPN2JQWjdLX0EiLCJpYXQiOjE3MzQyNjAwNjV9.t4-7yE3NAYU-DKzICkHP4C4VXfJcMT58Ie9U3_pCVY0"
            )
        )
    }
}
package com.dicoding.wanmuhtd.storyapp.data

import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.remote.model.Story
import com.dicoding.wanmuhtd.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    fun getToken() = userPreference.getToken()

    suspend fun saveToken(token: String) = userPreference.saveToken(token)

    suspend fun getUserData(): Pair<String, String> {
        val name = userPreference.getName()
        val email = userPreference.getEmail()
        return Pair(name, email)
    }

    suspend fun saveUserData(name: String, email: String) {
        userPreference.saveUserData(name, email)
    }

    suspend fun getDetailStory(storyId: String) = apiService.getDetailStory(storyId)

    suspend fun getStories(): Result<List<Story>> {
        return try {
            val response = apiService.getStories()

            if (!response.error) {
                Result.success(response.listStory)
            } else {
                Result.failure(Exception("Error: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String) = apiService.login(email, password)
    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?,
    ) = apiService.uploadImage(file, description, lat, lon)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
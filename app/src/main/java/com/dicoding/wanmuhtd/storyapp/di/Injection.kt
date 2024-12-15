package com.dicoding.wanmuhtd.storyapp.di

import android.content.Context
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.local.room.StoryDatabase
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(pref, apiService, database)
    }
}
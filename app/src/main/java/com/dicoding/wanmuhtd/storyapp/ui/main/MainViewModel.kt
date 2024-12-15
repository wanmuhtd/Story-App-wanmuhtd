package com.dicoding.wanmuhtd.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.local.entity.Story

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getStory(): LiveData<PagingData<Story>> =
        repository.getPagingStory().cachedIn(viewModelScope)

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }
}
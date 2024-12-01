package com.dicoding.wanmuhtd.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.remote.model.Story
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyList = MutableLiveData<Result<List<Story>>>()
    val storyList: LiveData<Result<List<Story>>> = _storyList

    fun getStories() {
        viewModelScope.launch {
            _storyList.value = repository.getStories()
        }
    }

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }
}
package com.dicoding.wanmuhtd.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.local.entity.Story
import kotlinx.coroutines.launch

class MapsViewModel (private val repository: UserRepository) : ViewModel() {
    private val _storyList = MutableLiveData<Result<List<Story>>>()
    val storyList: LiveData<Result<List<Story>>> = _storyList

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            _storyList.value = repository.getStoriesWithLocation()
        }
    }
}
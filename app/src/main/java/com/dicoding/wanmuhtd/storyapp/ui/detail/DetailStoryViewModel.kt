package com.dicoding.wanmuhtd.storyapp.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.local.entity.Story
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyDetail = MutableLiveData<Result<Story>>()
    val storyDetail: LiveData<Result<Story>> get() = _storyDetail

    fun getStoryDetail(storyId: String, context: Context) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(storyId)
                if (response.error) {
                    _storyDetail.postValue(Result.failure(Exception(response.message)))
                } else {
                    _storyDetail.postValue(Result.success(response.story))
                }
            } catch (e: HttpException) {
                _storyDetail.postValue(Result.failure(e))
            } catch (e: IOException) {
                _storyDetail.postValue(Result.failure(Exception(context.getString(R.string.network_error_please_check_your_connection))))
            }
        }
    }
}
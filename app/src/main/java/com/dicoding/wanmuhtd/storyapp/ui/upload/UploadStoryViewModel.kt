package com.dicoding.wanmuhtd.storyapp.ui.upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.ResultState
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.remote.model.MessageResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult: LiveData<ResultState<MessageResponse>> = _responseResult

    var currentImageUri: Uri? = null

    fun uploadStory(
        multipartBody: MultipartBody.Part,
        requestBodyDescription: RequestBody,
        latRequestBody: RequestBody?,
        lonRequestBody: RequestBody?,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                repository.getToken().collect { token ->
                    if (token != null) {
                        val response = repository.uploadStory(
                            multipartBody,
                            requestBodyDescription,
                            latRequestBody,
                            lonRequestBody
                        )
                        _responseResult.value = ResultState.Success(response)
                    } else {
                        _responseResult.value = ResultState.Error(context.getString(R.string.error_token_not_found))
                    }
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
                _responseResult.value = ResultState.Error(errorResponse.message)
            }
        }
    }
}
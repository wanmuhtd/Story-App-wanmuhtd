package com.dicoding.wanmuhtd.storyapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.di.Injection
import com.dicoding.wanmuhtd.storyapp.ui.detail.DetailStoryViewModel
import com.dicoding.wanmuhtd.storyapp.ui.login.LoginViewModel
import com.dicoding.wanmuhtd.storyapp.ui.main.MainViewModel
import com.dicoding.wanmuhtd.storyapp.ui.profilesetting.ProfileSettingViewModel
import com.dicoding.wanmuhtd.storyapp.ui.signup.SignupViewModel
import com.dicoding.wanmuhtd.storyapp.ui.upload.UploadStoryViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val pref: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileSettingViewModel::class.java) -> {
                ProfileSettingViewModel(repository, pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreference): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}
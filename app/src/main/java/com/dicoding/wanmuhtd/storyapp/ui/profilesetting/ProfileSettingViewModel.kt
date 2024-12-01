package com.dicoding.wanmuhtd.storyapp.ui.profilesetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import kotlinx.coroutines.launch

class ProfileSettingViewModel(
    private val repository: UserRepository,
    private val pref: UserPreference,
) : ViewModel() {
    private val _userData = MutableLiveData<Pair<String, String>>()
    val userData: LiveData<Pair<String, String>> = _userData

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun getUserData() {
        viewModelScope.launch {
            val data = repository.getUserData()
            _userData.value = data
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
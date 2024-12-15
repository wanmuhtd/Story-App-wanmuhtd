package com.dicoding.wanmuhtd.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityMainBinding
import com.dicoding.wanmuhtd.storyapp.di.Injection
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.ui.map.MapsActivity
import com.dicoding.wanmuhtd.storyapp.ui.profilesetting.ProfileSettingActivity
import com.dicoding.wanmuhtd.storyapp.ui.profilesetting.ProfileSettingViewModel
import com.dicoding.wanmuhtd.storyapp.ui.upload.UploadStoryActivity
import com.dicoding.wanmuhtd.storyapp.ui.welcome.WelcomeActivity
import com.dicoding.wanmuhtd.storyapp.utils.adapter.LoadingStateAdapter
import com.dicoding.wanmuhtd.storyapp.utils.adapter.StoryAdapter
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var settingViewModel: ProfileSettingViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        settingViewModel = ViewModelProvider(this, viewModelFactory)[ProfileSettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        supportActionBar?.title = (R.string.app_name.toString())

        storyAdapter = StoryAdapter()

        viewModel.getToken().observe(this) { token ->
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                loadStories()
            }
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        setupAction()
    }

    private fun loadStories() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getStory().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }


    private fun setupAction() {
        val addStoryButton: MaterialCardView = findViewById(R.id.action_add_story)
        addStoryButton.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }

        val mapButton: ImageView = findViewById(R.id.action_map)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val profileButton: ImageView = findViewById(R.id.action_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_app_bar)
        toolbar.setOnClickListener {
            scrollToTop()
        }
    }

    private fun scrollToTop() {
        binding.apply {
            rvStory.smoothScrollToPosition(0)
        }
    }
}
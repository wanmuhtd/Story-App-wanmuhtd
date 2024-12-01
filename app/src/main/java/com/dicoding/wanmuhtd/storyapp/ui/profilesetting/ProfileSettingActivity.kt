package com.dicoding.wanmuhtd.storyapp.ui.profilesetting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityProfileSettingBinding
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.ui.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

class ProfileSettingActivity : AppCompatActivity() {
    val pref = UserPreference.getInstance(dataStore)
    private val viewModel by viewModels<ProfileSettingViewModel> {
        ViewModelFactory.getInstance(this, pref)
    }
    private lateinit var binding: ActivityProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserData()
        viewModel.userData.observe(this) { userData ->
            val (name, email) = userData
            binding.tvName.text = name
            binding.tvEmail.text = email
        }

        setupTheme()
        setupAction()
    }

    private fun setupTheme() {
        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setupAction() {
        val toolbar: MaterialToolbar = findViewById(R.id.my_app_bar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        supportActionBar?.title = getString(R.string.title_profile_setting_page)

        val addStoryButton: MaterialCardView = findViewById(R.id.action_add_story)
        addStoryButton.visibility = View.GONE

        val profileButton: ImageView = findViewById(R.id.action_profile)
        profileButton.visibility = View.GONE

        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.logout()
                val intent = Intent(this@ProfileSettingActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }
}
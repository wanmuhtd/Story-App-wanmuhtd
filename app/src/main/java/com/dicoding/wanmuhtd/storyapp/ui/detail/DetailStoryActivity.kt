package com.dicoding.wanmuhtd.storyapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.data.remote.model.Story
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityDetailBinding
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.helper.withDateFormat
import com.dicoding.wanmuhtd.storyapp.ui.profilesetting.ProfileSettingActivity
import com.dicoding.wanmuhtd.storyapp.ui.upload.UploadStoryActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

class DetailStoryActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this, pref)
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)

        loadStoryDetail(storyId.toString())
        setupAction(storyId)
    }

    private fun setupAction(storyId: String?) {
        val toolbar: MaterialToolbar = findViewById(R.id.my_app_bar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        val addStoryButton: MaterialCardView = findViewById(R.id.action_add_story)
        addStoryButton.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }

        val profileButton: ImageView = findViewById(R.id.action_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        binding.btnReloadStory.setOnClickListener {
            loadStoryDetail(storyId.toString())
        }
    }

    private fun displayStoryDetail(story: Story) {
        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl.toUri())
                .centerCrop()
                .into(ivStoryImage)

            tvStoryName.text = story.name
            tvStoryDescription.text = story.description
            tvStoryDate.text = story.createdAt.withDateFormat()
        }
    }

    private fun loadStoryDetail(storyId: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.detailStoryContainer.visibility = View.GONE
        binding.btnReloadStory.visibility = View.GONE

        viewModel.getStoryDetail(storyId, this)
        viewModel.storyDetail.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess { story ->
                binding.btnReloadStory.visibility = View.GONE
                binding.detailStoryContainer.visibility = View.VISIBLE
                displayStoryDetail(story)

            }
            result.onFailure { error ->
                binding.btnReloadStory.visibility = View.VISIBLE
                binding.detailStoryContainer.visibility = View.GONE
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}
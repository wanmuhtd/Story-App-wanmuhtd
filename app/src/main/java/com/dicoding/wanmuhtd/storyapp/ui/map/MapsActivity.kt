package com.dicoding.wanmuhtd.storyapp.ui.map

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityMapsBinding
import com.dicoding.wanmuhtd.storyapp.di.Injection
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(dataStore)
        val userRepository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(userRepository, pref)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapsViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStoriesWithLocation()

        setupAction()
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

        supportActionBar?.title = getString(R.string.title_map_page)

        val addStoryButton: MaterialCardView = findViewById(R.id.action_add_story)
        addStoryButton.visibility = View.GONE

        val profileButton: ImageView = findViewById(R.id.action_profile)
        profileButton.visibility = View.GONE

        val mapButton: ImageView = findViewById(R.id.action_map)
        mapButton.visibility = View.GONE

        binding.retryButton.setOnClickListener {
            viewModel.getStoriesWithLocation()
            binding.progressBar.visibility = View.VISIBLE
            binding.retryButton.visibility = View.GONE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        viewModel.storyList.observe(this) { result ->
            binding.progressBar.visibility = View.VISIBLE
            binding.retryButton.visibility = View.GONE
            result.onFailure {
                Toast.makeText(this, getString(R.string.failed_load_stories), Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.VISIBLE
            }
            result.onSuccess { stories ->
                for (story in stories) {
                    val latLng = LatLng(story.lat.toDouble(), story.lon.toDouble())
                    mMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}
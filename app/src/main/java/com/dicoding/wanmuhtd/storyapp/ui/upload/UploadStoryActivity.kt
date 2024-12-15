package com.dicoding.wanmuhtd.storyapp.ui.upload

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.ResultState
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityUploadStoryBinding
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.helper.getImageUri
import com.dicoding.wanmuhtd.storyapp.helper.imageCompressor
import com.dicoding.wanmuhtd.storyapp.helper.uriToFile
import com.dicoding.wanmuhtd.storyapp.ui.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadStoryActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private lateinit var binding: ActivityUploadStoryBinding
    private val viewModel: UploadStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this, pref)
    }
    private var currentImageUri: Uri? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currentImageUri?.let {
            currentImageUri = it
            showImage()
        }

        val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(
            this@UploadStoryActivity,
            R.style.MaterialAlertDialog_Rounded
        )
        builder.setView(R.layout.layout_loading)
        val dialog: AlertDialog = builder.create()

        val intentMain = Intent(this, MainActivity::class.java)

        viewModel.responseResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    showToast(result.error)
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intentMain)
                    finish()
                    showToast(getString(R.string.upload_success))
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }

        setupAction()
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)

        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri = currentImageUri
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            viewModel.currentImageUri = currentImageUri
            showImage()
        }
    }

    private fun showImage() {
        if (currentImageUri != null) {
            binding.ivStoryImagePlaceholder.visibility = View.VISIBLE
            binding.ivPreviewImageContainer.visibility = View.GONE
            binding.ivStoryImagePlaceholder.setImageURI(currentImageUri)
        } else {
            binding.ivStoryImagePlaceholder.visibility = View.GONE
            binding.ivPreviewImageContainer.visibility = View.VISIBLE
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                println("Izin lokasi ditolak.")
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener<Location> { location ->
            if (location != null) {
                lat = location.latitude
                lon = location.longitude
                println("Latitude: $lat, Longitude: $lon")
            } else {
                println("Lokasi tidak ditemukan.")
            }
        })
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).imageCompressor()
            val description = binding.descEditText.text.toString()
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val latRequestBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
            val lonRequestBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModel.uploadStory(
                multipartBody,
                requestBodyDescription,
                latRequestBody,
                lonRequestBody,
                context = this
            )
            println("Latitude: $lat, Longitude: $lon")
        } ?: showToast(getString(R.string.no_image_selected))
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptForLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
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

        supportActionBar?.title = getString(R.string.title_add_new_story)

        val addStoryButton: MaterialCardView = findViewById(R.id.action_add_story)
        addStoryButton.visibility = View.GONE

        val profileButton: ImageView = findViewById(R.id.action_profile)
        profileButton.visibility = View.GONE

        val mapButton: ImageView = findViewById(R.id.action_map)
        mapButton.visibility = View.GONE

        binding.apply {
            cbLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (isLocationEnabled()) {
                        getLocation()
                    } else {
                        promptForLocationSettings()
                    }
                } else {
                    lat = null
                    lon = null
                }
            }

            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnUpload.setOnClickListener { uploadStory() }
        }
    }

}
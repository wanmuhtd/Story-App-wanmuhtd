package com.dicoding.wanmuhtd.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.wanmuhtd.storyapp.R
import com.dicoding.wanmuhtd.storyapp.data.ResultState
import com.dicoding.wanmuhtd.storyapp.data.pref.UserPreference
import com.dicoding.wanmuhtd.storyapp.data.pref.dataStore
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityLoginBinding
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.ui.main.MainActivity
import com.dicoding.wanmuhtd.storyapp.ui.signup.SignupActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LoginActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, pref)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            tvSignup.setOnClickListener {
                val signupActivity = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(signupActivity)
            }

            btnLogin.setOnClickListener {
                if (edLoginEmail.text!!.isNotEmpty() && edLoginPassword.text?.length!! >= 8) {
                    viewModel.login(
                        email = edLoginEmail.text.toString(),
                        password = edLoginPassword.text.toString()
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.please_fill_the_form_correctly),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val builder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(this@LoginActivity, R.style.MaterialAlertDialog_Rounded)
            builder.setView(R.layout.layout_loading)
            val dialog: AlertDialog = builder.create()

            viewModel.responseResult.observe(this@LoginActivity) { response ->
                when (response) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultState.Success -> {
                        dialog.dismiss()
                        val homeActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        homeActivity.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(homeActivity)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }
}
package com.dicoding.wanmuhtd.storyapp.ui.signup

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
import com.dicoding.wanmuhtd.storyapp.databinding.ActivitySignupBinding
import com.dicoding.wanmuhtd.storyapp.helper.ViewModelFactory
import com.dicoding.wanmuhtd.storyapp.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignupActivity : AppCompatActivity() {
    private val pref = UserPreference.getInstance(dataStore)
    private lateinit var binding: ActivitySignupBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    private val viewModel: SignupViewModel by viewModels<SignupViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupAction()
    }


    private fun setupAction() {
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            }

            btnSignup.setOnClickListener {
                if (edRegisterEmail.text!!.isNotEmpty() && edRegisterName.text!!.isNotEmpty() && edRegisterPassword.text?.length!! >= 8) {
                    viewModel.register(
                        name = edRegisterName.text.toString(),
                        email = edRegisterEmail.text.toString(),
                        password = edRegisterPassword.text.toString()
                    )
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        getString(R.string.please_fill_the_form_correctly),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            val builder: AlertDialog.Builder =
                MaterialAlertDialogBuilder(this@SignupActivity, R.style.MaterialAlertDialog_Rounded)
            builder.setView(R.layout.layout_loading)
            val dialog: AlertDialog = builder.create()

            viewModel.responseResult.observe(this@SignupActivity) { response ->
                when (response) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@SignupActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultState.Success -> {
                        dialog.dismiss()
                        val homeActivity = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(homeActivity)
                        finish()
                    }
                }
            }
        }
    }
}
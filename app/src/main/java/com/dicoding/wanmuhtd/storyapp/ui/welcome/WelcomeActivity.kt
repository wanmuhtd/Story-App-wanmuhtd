package com.dicoding.wanmuhtd.storyapp.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.wanmuhtd.storyapp.databinding.ActivityWelcomeBinding
import com.dicoding.wanmuhtd.storyapp.ui.login.LoginActivity
import com.dicoding.wanmuhtd.storyapp.ui.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupAction()
    }

    private fun setupAction() {
        binding.tvLogIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
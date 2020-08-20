package dev.blank.sketchme.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.blank.sketchme.R
import dev.blank.sketchme.data.local.preferences.UserPreferences
import dev.blank.sketchme.data.model.User
import dev.blank.sketchme.data.repository.UserRepository
import dev.blank.sketchme.databinding.ActivitySplashBinding
import dev.blank.sketchme.ui.MainActivity
import dev.blank.sketchme.ui.login.LoginActivity
import dev.blank.sketchme.viewmodel.user.UserViewModel
import dev.blank.sketchme.viewmodel.user.UserViewModelFactory

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        binding.ivLogo.animation = animation
        val userPreferences = UserPreferences(this)
        val userRepository = UserRepository(userPreferences)
        val viewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)
        viewModel.user?.observe(this, { user: User? ->
            val intent: Intent = if (user!!.isStatusLogin) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            Looper.myLooper()?.let { Handler(it).postDelayed({ startActivity(intent) }, 1000) }
        })
    }
}
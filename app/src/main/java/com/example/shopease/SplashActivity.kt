package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashLogo = findViewById<ImageView>(R.id.splash_logo)
        val splashAppName = findViewById<TextView>(R.id.splash_app_name)

        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_animation)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_text_animation)

        splashLogo.startAnimation(logoAnimation)
        splashAppName.startAnimation(textAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 2 second delay
    }
}

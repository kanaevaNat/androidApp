package com.kanaeva.mobilecursach

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity: AppCompatActivity()  {
    private val SPLASH_DISPLAT_LENGTH = 1000L; // время отображения в мс

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler().postDelayed(Runnable { // По истечении времени, запускаем главный активити, а Splash Screen закрываем
            val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            this@SplashScreenActivity.startActivity(mainIntent)
            finish() }, SPLASH_DISPLAT_LENGTH)
    }
}

package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set your splash screen layout
        setContentView(R.layout.activity_splash)

        // Delay for 5 seconds and then launch the main activity
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(5000)  // 5 seconds delay
                    val intent = Intent(baseContext, SlideshowActivity::class.java)
                    startActivity(intent)
                    finish()  // Finish splash activity
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
package com.example.chronolog_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LogHours : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var elapsedTimeText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private var timeSaved = ""

    private var handler: Handler = Handler()
    private var isTimerRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_hours)

        // Initialize views
        timerText = findViewById(R.id.timer_text)
        elapsedTimeText = findViewById(R.id.elapsed_time_text)
        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)

        startButton.setOnClickListener {
            startTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.postDelayed(timerRunnable, 1000)
            isTimerRunning = true
            startButton.isEnabled = false
            stopButton.isEnabled = true
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        isTimerRunning = false
        elapsedTime = System.currentTimeMillis() - startTime
        startButton.isEnabled = true
        stopButton.isEnabled = false
        timeSaved = timerText.text.toString() // Store the formatted time string
        elapsedTimeText.text = timeSaved // Set the text at the bottom TextView
        val time = timerText.text.toString()
        val sharedPreferences = getSharedPreferences("SendHours", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Time", time)
        editor.apply()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            updateTimerText()
            handler.postDelayed(this, 1000)
        }
    }

    private fun updateTimerText() {
        val seconds = elapsedTime / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val timeString = "%02d:%02d:%02d".format(hours, minutes % 60, seconds % 60)
        timerText.text = timeString
    }

}

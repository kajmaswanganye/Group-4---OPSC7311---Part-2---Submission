package com.example.chronolog_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogHours : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var elapsedTimeText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var saveButton: Button

    private var timeSaved = ""

    private var handler: Handler = Handler()
    private var isTimerRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_hours)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().reference

        // Initialize views
        timerText = findViewById(R.id.timer_text)
        elapsedTimeText = findViewById(R.id.elapsed_time_text)
        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)
        saveButton = findViewById(R.id.save_button)

        startButton.setOnClickListener {
            startTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        saveButton.setOnClickListener {
            saveTimer()
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.postDelayed(timerRunnable, 1000)
            isTimerRunning = true
            startButton.isEnabled = false
            stopButton.isEnabled = true
            saveButton.isEnabled = false
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        isTimerRunning = false
        elapsedTime = System.currentTimeMillis() - startTime
        startButton.isEnabled = true
        stopButton.isEnabled = false
        saveButton.isEnabled = true
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

    private fun saveTimer() {
        val endTime = System.currentTimeMillis()
        val duration = elapsedTime / 1000 // duration in seconds
        val startTimeFormatted = java.text.SimpleDateFormat("HH:mm:ss").format(startTime)
        val endTimeFormatted = java.text.SimpleDateFormat("HH:mm:ss").format(endTime)

        val taskData = mapOf(
            "startTime" to startTimeFormatted,
            "duration" to duration,
            "endTime" to endTimeFormatted
        )

        database.child("tasks").push().setValue(taskData)
            .addOnSuccessListener {
                // Data saved successfully
                showDialog("Task logged successfully.")
            }
            .addOnFailureListener { e ->
                // Failed to save data
                showDialog("Failed to log task: ${e.message}")
            }
    }

    private fun showDialog(message: String) {
        // Create an AlertDialog
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK button is clicked
                dialog.dismiss()
            }
            .show()
    }
}

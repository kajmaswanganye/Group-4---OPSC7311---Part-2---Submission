package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Initialize views
        val leaderboardTextView: TextView = findViewById(R.id.leaderboard)
        val messagesTextView: TextView = findViewById(R.id.messages)

        // Set click listener for Leaderboard TextView
        leaderboardTextView.setOnClickListener {
            // Navigate to LeaderboardActivity
            startActivity(Intent(this@ProfileActivity, LeaderboardActivity::class.java))
        }

        // Set click listener for Messages TextView
       // messagesTextView.setOnClickListener {
            // Navigate to MessagesActivity (replace with your actual activity)
           // startActivity(Intent(this@ProfileActivity, MessagesActivity::class.java))
        //}
    }
}

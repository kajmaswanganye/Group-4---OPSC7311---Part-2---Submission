package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        usernameTextView = findViewById(R.id.username)
        fullNameTextView = findViewById(R.id.fullName)
        emailTextView = findViewById(R.id.email)
        phoneNumberTextView = findViewById(R.id.phoneNumber)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        fetchUserDetails()

        // Initialize views
        val leaderboardTextView: TextView = findViewById(R.id.leaderboard)

        // Set click listener for Leaderboard TextView
        leaderboardTextView.setOnClickListener {
            // Navigate to LeaderboardActivity
            startActivity(Intent(this@ProfileActivity, LeaderboardActivity::class.java))
        }
    }

    private fun fetchUserDetails() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        val userReference = firebaseDatabase.getReference("users").child(userId)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        usernameTextView.text = userData.username
                        fullNameTextView.text = "Name: ${userData.fullName}"
                        emailTextView.text = "Email: ${userData.email}"
                        phoneNumberTextView.text = "Phone number: ${userData.phoneNumber}"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors
            }
        })
    }
}
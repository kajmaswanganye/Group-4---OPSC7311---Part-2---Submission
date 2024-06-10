package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupConfirmPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        fullName = findViewById(R.id.fullName)
        email = findViewById(R.id.email)
        phoneNumber = findViewById(R.id.phoneNumber)
        signupUsername = findViewById(R.id.signupUsername)
        signupPassword = findViewById(R.id.signupPassword)
        signupConfirmPassword = findViewById(R.id.signupConfirmPassword)
        btnSignUp = findViewById(R.id.btnsignup)

        btnSignUp.setOnClickListener {
            val name = fullName.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val phone = phoneNumber.text.toString().trim()
            val username = signupUsername.text.toString().trim()
            val password = signupPassword.text.toString()
            val confirmPassword = signupConfirmPassword.text.toString()

            if (name.isNotEmpty() && userEmail.isNotEmpty() && phone.isNotEmpty() &&
                username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {

                if (password == confirmPassword) {
                    signupUser(name, userEmail, phone, username, password)
                } else {
                    Toast.makeText(this@SignupActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@SignupActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Already a user? Login" click
        val tvAlreadyAUser = findViewById<TextView>(R.id.tvAlreadyAUser)
        tvAlreadyAUser.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
    }

    private fun signupUser(name: String, email: String, phone: String, username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UserData(id, name, email, phone, username, password)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity, "User already exists", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

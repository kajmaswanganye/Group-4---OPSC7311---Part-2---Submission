package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextEmail = findViewById(R.id.editTextSignUpEmail)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextSignUpPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirm)
        btnSignUp = findViewById(R.id.btnsignup)

        btnSignUp.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (validateInputs(email, username, password, confirmPassword)) {
                // Perform signup process (e.g., send data to server or save locally)
                // For demo, display a toast message
                Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()

                // Example: Navigate to HomeActivity after successful signup
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Handle "Already a user? Login" click
        val tvAlreadyAUser = findViewById<TextView>(R.id.tvAlreadyAUser)
        tvAlreadyAUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validateInputs(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Enter a valid email address"
            return false
        }

        if (username.isEmpty()) {
            editTextUsername.error = "Enter a username"
            return false
        }

        if (password.length < 6) {
            editTextPassword.error = "Password must be at least 6 characters long"
            return false
        }

        if (password != confirmPassword) {
            editTextConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }
}
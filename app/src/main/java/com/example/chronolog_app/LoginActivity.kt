package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextLoginEmail)
        editTextPassword = findViewById(R.id.editTextLoginPassword)
        btnLogin = findViewById(R.id.btnlogin)
        tvForgotPassword = findViewById(R.id.tvRecoverPassword)
        tvSignUp = findViewById(R.id.tvAlreadyAUser)

        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()

            if (validateInputs(email, password)) {
                // Perform login process (e.g., send data to server or check locally)
                // For demo, display a toast message
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // Example: Navigate to HomeActivity after successful login
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        tvForgotPassword.setOnClickListener {
            // Implement logic to handle forgot password (e.g., show dialog or navigate to recovery screen)
            Toast.makeText(this, "Forgot Password clicked!", Toast.LENGTH_SHORT).show()
        }

        tvSignUp.setOnClickListener {
            // Navigate to SignupActivity when "New user? Sign up" is clicked
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Enter a valid email address"
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Enter a password"
            return false
        }

        return true
    }
}

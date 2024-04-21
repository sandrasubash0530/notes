package com.example.notes.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.notes.database.data.User
import com.example.notes.R
import com.example.notes.database.data.AppDatabase

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailEditText = findViewById<EditText>(R.id.emailEt)
        val passwordEditText = findViewById<EditText>(R.id.passET)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPassEt)
        val signUpButton = findViewById<Button>(R.id.button)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password != confirmPassword) {
                showToast("Passwords do not match")
                return@setOnClickListener
            }

            val user = createUser(email, password)
            val userDao = AppDatabase.getInstance(this).userDao()

            GlobalScope.launch(Dispatchers.IO) {
                if (userDao.getUserByEmail(email) != null) {
                    // User already exists
                    showToast("User already exists")
                } else {
                    // Insert the user into the database
                    userDao.insert(user)
                    showToast("Sign up successful")
                    navigateToLogin()
                }
            }
        }
    }

    private fun createUser(email: String, password: String): User {
        // Create and return a User object
        return User(username = email, email = email, password = password)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to SignUpActivity when pressing back
    }
}

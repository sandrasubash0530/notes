package com.example.notes.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.database.data.AppDatabase
import com.example.notes.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passET)
        val signInButton = findViewById<Button>(R.id.button)
        val signUpText = findViewById<TextView>(R.id.textView)

        // Handle sign-in button click
        signInButton.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()

            // Validate email and password
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Perform authentication
                authenticateUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle sign-up text click
        signUpText.setOnClickListener {
            // Navigate to the sign-up screen
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val userDao = AppDatabase.getDatabase(this).userDao()

        // Launch a coroutine for database operations
        CoroutineScope(Dispatchers.IO).launch {
            // Check if the user exists in the database
            val user = userDao.getUserByEmail(email)

            if (user != null) {
                // User exists, check if the password matches
                if (user.password == password) {
                    // Password matches, authentication successful
                    // Start MainActivity
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish() // Finish the current activity
                } else {
                    // Password does not match, display error message
                    runOnUiThread {
                        Toast.makeText(
                            this@SignInActivity,
                            "Incorrect password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                // User does not exist, prompt the user to sign up
                runOnUiThread {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign Up")
                        .setMessage("No account found with this email. Do you want to sign up?")
                        .setPositiveButton("Sign Up") { dialog, which ->
                            // Navigate to sign up activity
                            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
        }
    }
}

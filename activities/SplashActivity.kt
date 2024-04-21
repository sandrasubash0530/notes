package com.example.notes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notes.R
import com.example.notes.activities.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simulate a delay before deciding which activity to navigate to
        Thread.sleep(3000)

        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
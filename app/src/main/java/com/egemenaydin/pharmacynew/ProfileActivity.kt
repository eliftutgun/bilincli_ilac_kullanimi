package com.egemenaydin.pharmacynew

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val emailTextView = findViewById<TextView>(R.id.textViewEmail)
        val changePasswordButton = findViewById<Button>(R.id.buttonChangePassword)
        val backButton = findViewById<Button>(R.id.buttonBack)

        val sharedPref = getSharedPreferences("userData", MODE_PRIVATE)
        val email = sharedPref.getString("email", "Mail bulunamadı")
        emailTextView.text = "E-mailiniz : $email"

        changePasswordButton.setOnClickListener {
            val intent = Intent(this, SifreSifirlaActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}


package com.egemenaydin.pharmacynew

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        // Tema ayarını uygula
        val preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = preferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val signUpButton = findViewById<Button>(R.id.buttonRegister)
        val forgotPassword = findViewById<TextView>(R.id.textViewForgotPassword)


        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Eğer mail veya şifre boşsa uyarı göster
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Hata", "E-posta ve şifreyi boş bırakmayınız.")
            } else {
                // E-posta ve şifreyi kontrol et
                if (dbHelper.checkUser(email, password)) {
                    val sharedPref = getSharedPreferences("userData", MODE_PRIVATE)
                    sharedPref.edit().putString("email", email).apply()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    showAlert("Hata", "E-posta veya şifre hatalı")
                }
            }
        }

        //kayıt ol sayfasına yönlendir
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //şifre sıfırlama sayfasına yönlendir
        forgotPassword.setOnClickListener {
            val intent = Intent(this, SifreSifirlaActivity::class.java)
            startActivity(intent)
        }


    }

    // AlertDialog fonksiyonu
    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}

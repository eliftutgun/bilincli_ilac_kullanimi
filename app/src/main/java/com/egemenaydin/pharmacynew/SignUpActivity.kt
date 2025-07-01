package com.egemenaydin.pharmacynew

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        dbHelper = DBHelper(this)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        val loginLink = findViewById<TextView>(R.id.textViewLoginLink)

        signUpButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // E-posta adresinin boş olmadığını kontrol et
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // E-posta veritabanında var mı kontrol et
                if (dbHelper.checkEmailExists(email)) {
                    // Eğer e-posta mevcutsa, kullanıcıya uyarı ver
                    showEmailExistsDialog()
                } else {
                    // E-posta veritabanında yoksa, yeni kullanıcıyı ekleyebiliriz
                    addUserToDatabase(email, password)
                }
            } else {
                // Alanlar boşsa kullanıcıya uyarı ver
                showEmptyFieldsDialog()
            }
        }
        // "Zaten hesabım var, giriş yap" yazısına tıklanabilirlik ekle
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // MainActivity'ye geçtikten sonra SignUpActivity'yi kapat
        }

    }

    // E-posta zaten varsa uyarı diyalogunu göster
    private fun showEmailExistsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hata")
        builder.setMessage("Bu e-posta adresi zaten kullanılıyor. Lütfen başka bir e-posta girin.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss() // Dialogu kapat
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Alanlar boşsa uyarı diyalogunu göster
    private fun showEmptyFieldsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hata")
        builder.setMessage("E-posta ve şifre alanlarını boş bırakmayınız.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss() // Dialogu kapat
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Yeni kullanıcıyı veritabanına ekle
    private fun addUserToDatabase(email: String, password: String) {
        val db = dbHelper.writableDatabase
        val query = "INSERT INTO users (email, password) VALUES (?, ?)"
        val statement = db.compileStatement(query)
        statement.bindString(1, email)
        statement.bindString(2, password)

        statement.executeInsert()
        db.close()

        // Kayıt başarılı, ana ekrana yönlendir
        finish() // SignUpActivity'yi kapat
    }
}

package com.egemenaydin.pharmacynew

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // MainActivity'den gelen kullanıcı mail adresini alma
        val email = intent.getStringExtra("email")

        // Hoş geldiniz mesajı
        val welcomeMessage = findViewById<TextView>(R.id.textViewWelcome)
        welcomeMessage.text = "Hoş geldiniz, $email!"

        val profileImage = findViewById<ImageView>(R.id.imageViewProfile)
        profileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("email", email) // giriş yapan mail
            startActivity(intent)
        }
        val qrImage = findViewById<ImageView>(R.id.imageViewQR)
        qrImage.setOnClickListener {
            val intent = Intent(this, QrScanActivity::class.java)
            startActivity(intent)
        }
        val prospectusImage = findViewById<ImageView>(R.id.imageViewProspektus)
        prospectusImage.setOnClickListener {
            val intent = Intent(this, ProspectusActivity::class.java)
            startActivity(intent)
        }
        val myMedicinesImageView = findViewById<ImageView>(R.id.imageViewIlaclarim)

        myMedicinesImageView.setOnClickListener {
            val intent = Intent(this, MyMedicinesActivity::class.java)
            startActivity(intent)
        }
        val medicineButton = findViewById<ImageView>(R.id.imageViewIlaclar)
        medicineButton.setOnClickListener {
            val intent = Intent(this, MedicinesActivity::class.java)
            startActivity(intent)
        }
        val eczanelerImage = findViewById<ImageView>(R.id.imageViewEczane)
        eczanelerImage.setOnClickListener {
            val intent = Intent(this, EzcaneActivity::class.java)
            startActivity(intent)
        }
        val alarmImage = findViewById<ImageView>(R.id.imageViewAlarm)
        alarmImage.setOnClickListener {
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }
        val ayarlarImage = findViewById<ImageView>(R.id.imageViewAyarlar)
        ayarlarImage.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    // Geri tuşuna basıldığında gösterilecek AlertDialog
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Çıkış")
        builder.setMessage("Hesabınızdan çıkmak istiyor musunuz?")
        builder.setPositiveButton("Evet") { _, _ ->
            // Eğer kullanıcı evet derse, MainActivity'ye geçiş yap
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // HomeActivity'yi kapat
        }
        builder.setNegativeButton("Hayır") { dialog, _ ->
            dialog.dismiss() // Eğer kullanıcı hayır derse, dialogu kapat
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}

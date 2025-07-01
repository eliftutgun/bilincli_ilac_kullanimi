package com.egemenaydin.pharmacynew

import DBHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SifreSifirlaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifre_sifirla)

        dbHelper = DBHelper(this)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val newPasswordInput = findViewById<EditText>(R.id.editTextNewPassword)
        val resetButton = findViewById<Button>(R.id.buttonResetPassword)

        resetButton.setOnClickListener {
            val email = emailInput.text.toString()
            val newPassword = newPasswordInput.text.toString()

            // E-posta ve yeni şifre boş olmamalı
            if (email.isNotEmpty() && newPassword.isNotEmpty()) {
                // Veritabanında e-posta var mı kontrol et
                if (dbHelper.checkEmailExists(email)) {
                    // E-posta varsa, şifreyi güncelle
                    val updateSuccess = dbHelper.updatePassword(email, newPassword)

                    if (updateSuccess) {
                        // Şifre başarıyla güncellendiyse kullanıcıyı bilgilendir
                        showSuccessDialog()
                    } else {
                        // Şifre güncelleme başarısızsa, hata mesajı göster
                        showFailureDialog()
                    }
                } else {
                    // E-posta veritabanında yoksa, kullanıcıyı bilgilendir
                    showEmailNotFoundDialog()
                }
            } else {
                // E-posta ve şifre boşsa, uyarı mesajı göster
                showEmptyFieldsDialog()
            }
        }
    }

    // Şifre güncellenme başarısı
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Başarılı")
        builder.setMessage("Şifreniz başarıyla güncellendi.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
            finish() // Activity'yi kapat ve MainActivity'ye dön
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Şifre güncellenme başarısızsa
    private fun showFailureDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hata")
        builder.setMessage("Şifre güncellenirken bir hata oluştu.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // E-posta veritabanında yoksa kullanıcıya uyarı
    private fun showEmailNotFoundDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hata")
        builder.setMessage("Bu e-posta adresi kayıtlı değil.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Alanlar boşsa uyarı
    private fun showEmptyFieldsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hata")
        builder.setMessage("E-posta ve yeni şifre alanlarını boş bırakmayınız.")
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}

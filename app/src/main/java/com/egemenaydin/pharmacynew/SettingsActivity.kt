package com.egemenaydin.pharmacynew

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitch: Switch
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings) // Bu satır ilk olmalı!

        // Switch'leri tanımla
        val switchNotification = findViewById<Switch>(R.id.notificationSwitch)
        themeSwitch = findViewById(R.id.themeSwitch)

        // Bildirim ayarı
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isNotificationEnabled = sharedPreferences.getBoolean("notifications", true)
        switchNotification.isChecked = isNotificationEnabled

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
        }

        // Tema ayarı
        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = preferences.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDarkMode

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

}

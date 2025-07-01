package com.egemenaydin.pharmacynew

data class Reminder(
    val id: Int = 0,
    val medicineName: String,
    val hour: Int,
    val minute: Int,
    val repeat: Boolean,
    val requestCode: Int =0
)



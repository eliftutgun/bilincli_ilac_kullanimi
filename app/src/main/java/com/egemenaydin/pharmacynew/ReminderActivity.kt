package com.egemenaydin.pharmacynew
import ReminderAdapter
import ReminderDPHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egemenaydin.pharmacynew.R
import com.egemenaydin.pharmacynew.Reminder
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var dbHelper: ReminderDPHelper
    private lateinit var spinnerMedicines: Spinner
    private lateinit var timePicker: TimePicker
    private lateinit var checkBoxRepeat: CheckBox
    private lateinit var buttonAddReminder: Button
    private lateinit var recyclerViewReminders: RecyclerView
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // UI bileşenlerini bağla
        spinnerMedicines = findViewById(R.id.spinner)
        timePicker = findViewById(R.id.timePicker)
        checkBoxRepeat = findViewById(R.id.checkBoxRepeat)
        buttonAddReminder = findViewById(R.id.buttonAddReminder)
        recyclerViewReminders = findViewById(R.id.recyclerViewReminders)

        // DB helper'ı başlat
        dbHelper = ReminderDPHelper(this)

        // Spinner'ı doldur
        loadSpinner()

        // Alarm ekleme butonuna tıklandığında
        buttonAddReminder.setOnClickListener {
            checkExactAlarmPermission() // Alarm izni kontrolü ve hatırlatıcı ekleme işlemi burada yapılacak
        }

        // Hatırlatıcıları listele
        loadReminders()
        recyclerViewReminders.layoutManager = LinearLayoutManager(this)
    }

    private fun loadSpinner() {
        val medicineList = dbHelper.getMedicines()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, medicineList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedicines.adapter = adapter
    }

    private fun loadReminders() {
        val reminders = dbHelper.getAllReminders()
        if (::reminderAdapter.isInitialized) {
            reminderAdapter.updateList(reminders)
        } else {
            reminderAdapter = ReminderAdapter(reminders) { reminder ->
                dbHelper.deleteReminder(reminder.id)
                loadReminders() // Silindikten sonra listeyi yenile
            }
            recyclerViewReminders.adapter = reminderAdapter
        }
    }


    private fun checkExactAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 (API 31) ve sonrası
            if (alarmManager.canScheduleExactAlarms()) {
                // Tam alarm izni verilmiş, alarmı kurmaya devam et
                addReminderAndSetAlarm()
            } else {
                // Tam alarm izni verilmemiş, kullanıcıyı ayarlara yönlendirme
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
                Toast.makeText(
                    this,
                    "Tam alarm izni yok. Lütfen izinleri kontrol edin.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            // Android 12'den önce alarm izinleri daha esnek, alarmı kurmaya devam et
            addReminderAndSetAlarm()
        }
    }


    private fun addReminderAndSetAlarm() {
        val medicineName = spinnerMedicines.selectedItem.toString()
        val hour = if (Build.VERSION.SDK_INT >= 23) timePicker.hour else timePicker.currentHour
        val minute =
            if (Build.VERSION.SDK_INT >= 23) timePicker.minute else timePicker.currentMinute
        val repeat = checkBoxRepeat.isChecked

        // Hatırlatıcıyı veritabanına ekle
        val reminder =
            Reminder(medicineName = medicineName, hour = hour, minute = minute, repeat = repeat)
        val reminderId = dbHelper.insertReminder(
            reminder.medicineName,
            reminder.hour,
            reminder.minute,
            reminder.repeat,
            reminder.requestCode
        )

        // Alarmı kur
        setAlarm(reminderId, hour, minute, repeat)
        loadReminders()
    }

    private fun setAlarm(reminderId: Long, hour: Int, minute: Int, repeat: Boolean) {
        val context = applicationContext
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                scheduleAlarm(alarmManager, context, reminderId, hour, minute, repeat)
            } else {
                Toast.makeText(
                    context,
                    "Tam alarm izni yok. Lütfen izinleri kontrol edin.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            scheduleAlarm(alarmManager, context, reminderId, hour, minute, repeat)
        }
    }

    private fun scheduleAlarm(
        alarmManager: AlarmManager,
        context: Context,
        reminderId: Long,
        hour: Int,
        minute: Int,
        repeat: Boolean
    ) {
        val reminder = dbHelper.getReminderById(reminderId.toInt())
        val medicineName = reminder?.medicineName ?: "Unknown Medicine"

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("reminderId", reminderId)
            putExtra("medicine_name", medicineName)
        }

        val requestCode = reminderId.toInt() // `requestCode` değerini doğru alıyoruz
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Eğer zaman geçmişse, bir gün sonraya al
        val currentTime = Calendar.getInstance().timeInMillis
        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (repeat) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}

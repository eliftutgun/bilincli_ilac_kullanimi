import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.egemenaydin.pharmacynew.Reminder
import ReminderAdapter

class ReminderDPHelper(context: Context) : SQLiteOpenHelper(context, "pharmacy_medicines.db", null, 5) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE reminders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                medicine_name TEXT,
                hour INTEGER,
                minute INTEGER,
                repeat INTEGER,
                request_code INTEGER
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS reminders")
        onCreate(db)
    }

    // Hatırlatıcı ekleme fonksiyonu
    fun insertReminder(medicineName: String, hour: Int, minute: Int, repeat: Boolean, requestCode: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("medicine_name", medicineName)
            put("hour", hour)
            put("minute", minute)
            put("repeat", if (repeat) 1 else 0) // repeat'i Boolean'dan Int'e çeviriyoruz
            put("request_code", requestCode)
        }
        return db.insert("reminders", null, values)
    }

    fun getAllReminders(): List<Reminder> {
        val db = this.readableDatabase
        val cursor = db.query("reminders", null, null, null, null, null, null)
        val reminders = mutableListOf<Reminder>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val medicineName = getString(getColumnIndexOrThrow("medicine_name"))
                val hour = getInt(getColumnIndexOrThrow("hour"))
                val minute = getInt(getColumnIndexOrThrow("minute"))
                val repeat = getInt(getColumnIndexOrThrow("repeat")) == 1 // repeat'i Int'ten Boolean'a çeviriyoruz
                val requestCode = getInt(getColumnIndexOrThrow("request_code"))
                reminders.add(Reminder(id, medicineName, hour, minute, repeat, requestCode))
            }
        }
        cursor.close()
        return reminders
    }

    // Hatırlatıcı silme fonksiyonu
    fun deleteReminder(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete("reminders", "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Tek hatırlatıcıyı id ile almak
    fun getReminderById(id: Int): Reminder? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM reminders WHERE id = ?", arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("medicine_name"))
            val hour = cursor.getInt(cursor.getColumnIndexOrThrow("hour"))
            val minute = cursor.getInt(cursor.getColumnIndexOrThrow("minute"))
            val repeat = cursor.getInt(cursor.getColumnIndexOrThrow("repeat")) == 1
            val requestCode = cursor.getInt(cursor.getColumnIndexOrThrow("request_code"))
            val reminder = Reminder(id, name, hour, minute, repeat, requestCode)
            cursor.close()
            return reminder
        }
        cursor.close()
        return null
    }
    fun getMedicines(): List<String> {
        val db = this.readableDatabase
        val medicineList = mutableListOf<String>()

        val cursor = db.rawQuery("SELECT name FROM medicines", null)
        if (cursor.moveToFirst()) {
            do {
                val medicineName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                medicineList.add(medicineName)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return medicineList
    }
    fun addReminder(medicineName: String, hour: Int, minute: Int, repeat: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("medicine_name", medicineName)
            put("hour", hour)
            put("minute", minute)
            put("repeat", repeat)
        }
        db.insert("reminders", null, values)
    }


}


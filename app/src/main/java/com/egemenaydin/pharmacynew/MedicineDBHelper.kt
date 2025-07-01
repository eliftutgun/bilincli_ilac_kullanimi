import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import java.io.FileOutputStream
import java.io.IOException

class MedicineDBHelper(private val context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
) {
    companion object {
        private const val DB_NAME = "pharmacy_medicines.db"
        private const val DB_VERSION = 5
    }

    private val dbPath: String
        get() = context.getDatabasePath(DB_NAME).path

    init {
        copyDatabaseIfNeeded()
    }

    private fun copyDatabaseIfNeeded() {
        val dbFile = context.getDatabasePath(DB_NAME)

        if (!dbFile.exists()) {
            try {
                context.assets.open(DB_NAME).use { inputStream ->
                    FileOutputStream(dbFile).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Önceden oluşturulmuş, gerek yok.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Gerekirse buraya versiyon güncellemesi eklenebilir.
    }
    fun addToMyMedicines(medicine: Medicine): Boolean {
        val db = writableDatabase
        return try {
            val sql = "INSERT INTO medicines (id, name, usage, active_igredient, side_effects, uyarilar) VALUES (?, ?, ?, ?, ?)"
            val stmt = db.compileStatement(sql)
            stmt.bindLong(1, medicine.id.toLong())
            stmt.bindString(2, medicine.name)
            stmt.bindString(3, medicine.usage)
            stmt.bindString(4, medicine.etkenMadde)
            stmt.bindString(5, medicine.sideEffects)
            stmt.bindString(6, medicine.uyarilar)
            stmt.executeInsert()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }


    fun getAllMedicines(): List<Medicine> {
        val medicineList = mutableListOf<Medicine>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM medicines", null)
        if (cursor.moveToFirst()) {
            do {
                val medicine = Medicine(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    etkenMadde = cursor.getString(cursor.getColumnIndexOrThrow("active_igredient")),
                    usage = cursor.getString(cursor.getColumnIndexOrThrow("usage")),
                    sideEffects = cursor.getString(cursor.getColumnIndexOrThrow("side_effects")),
                    uyarilar = cursor.getString(cursor.getColumnIndexOrThrow("uyarilar"))
                )
                medicineList.add(medicine)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return medicineList
    }
}

data class Medicine(
    val id: Int,
    val name: String,
    val usage: String,
    val etkenMadde: String,
    val sideEffects: String,
    val uyarilar: String
)

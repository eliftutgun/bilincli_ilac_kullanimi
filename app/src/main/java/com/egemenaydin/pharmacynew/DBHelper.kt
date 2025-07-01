import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException

class DBHelper(private val context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
) {
    companion object {
        private const val DB_NAME = "pharmacy_users.db"
        private const val DB_VERSION = 1
    }

    private val dbPath: String
        get() = context.getDatabasePath(DB_NAME).path

    // Veritabanını kopyalamayı başlatan fonksiyon
    init {
        copyDatabaseIfNeeded()
    }

    // Veritabanı kopyalama işlemi
    fun copyDatabaseIfNeeded() {
        val dbFile = context.getDatabasePath(DB_NAME)

        // Veritabanı klasörünü oluştur
        val dbDir = dbFile.parentFile
        if (!dbDir.exists()) {
            if (dbDir.mkdirs()) {
                Log.d("DBHelper", "Veritabanı klasörü oluşturuldu.")
            } else {
                Log.e("DBHelper", "Veritabanı klasörü oluşturulamadı!")
                return
            }
        }

        // Veritabanı zaten varsa işlemi atla
        if (!dbFile.exists()) {
            try {
                Log.d("DBHelper", "Veritabanı kopyalanıyor...")
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
                Log.d("DBHelper", "Veritabanı başarıyla kopyalandı.")
            } catch (e: IOException) {
                Log.e("DBHelper", "Veritabanı kopyalanırken hata oluştu: ${e.message}")
                e.printStackTrace()
            }
        } else {
            Log.d("DBHelper", "Veritabanı zaten var, kopyalanmadı.")
        }
    }

    // Kullanıcı doğrulama işlemi
    fun checkUser(email: String, password: String): Boolean {
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM users WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Yeni bir kullanıcı ekleme fonksiyonu
    fun insertUser(email: String, password: String): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val contentValues = ContentValues().apply {
            put("email", email)
            put("password", password)
        }

        // Kullanıcıyı veritabanına ekle
        val result = db.insert("users", null, contentValues)
        db.close()

        // Eğer result -1 dönerse işlem başarısız olmuş demektir.
        return result != -1L
    }

    // Veritabanında bu e-posta adresinin olup olmadığını kontrol et
    fun checkEmailExists(email: String): Boolean {
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM users WHERE email = ?",
            arrayOf(email)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Kullanıcının şifresini güncelleyen fonksiyon
    fun updatePassword(email: String, newPassword: String): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("password", newPassword)

        val result = db.update("users", contentValues, "email = ?", arrayOf(email))
        db.close()
        return result > 0
    }
    data class Medicine(
        val id: Int,
        val name: String,
        val usage: String,
        val sideEffects: String,
        val uyarilar: String
    )

    fun getAllMedicines(): List<Medicine> {
        val medicines = mutableListOf<Medicine>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM medicines", null)
        if (cursor.moveToFirst()) {
            do {
                val medicine = Medicine(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    usage = cursor.getString(cursor.getColumnIndexOrThrow("usage")),
                    sideEffects = cursor.getString(cursor.getColumnIndexOrThrow("side_effects")),
                    uyarilar = cursor.getString(cursor.getColumnIndexOrThrow("uyarilar"))
                )
                medicines.add(medicine)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return medicines
    }



    // Veritabanı oluşturulurken kullanılan fonksiyon
    override fun onCreate(db: SQLiteDatabase?) {
        // Veritabanı önceden oluşturulmuş olduğundan burada bir işlem yapmaya gerek yok.
    }

    // Veritabanı sürümü değişirse yapılacak işlemler
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Veritabanı sürümünü güncellemek için burayı kullanabilirsin.
    }
}

package com.egemenaydin.pharmacynew

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProspectusDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pharmacy_medicines.db"
        private const val DATABASE_VERSION = 5
        const val TABLE_NAME = "ilac_prospectus"
        const val COLUMN_NAME = "name"
        const val COLUMN_BILGI = "prospectus"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_BILGI TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllProspectus(): List<String> {
        val prospectusList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_NAME FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                prospectusList.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return prospectusList
    }
    fun getProspectusByName(name: String): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_BILGI FROM $TABLE_NAME WHERE $COLUMN_NAME = ?", arrayOf(name))
        var bilgi = "Bilgi bulunamadı"
        if (cursor.moveToFirst()) {
            bilgi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BILGI))
        }
        cursor.close()
        db.close()
        return bilgi
    }

}

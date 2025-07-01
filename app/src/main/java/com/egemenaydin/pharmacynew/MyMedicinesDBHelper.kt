package com.egemenaydin.pharmacynew

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyMedicinesDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "my_medicines.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_NAME = "my_medicines"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addMedicine(name: String): Boolean {
            val db = writableDatabase

            // Daha önce eklenmiş mi kontrol et
            val cursor = db.rawQuery("SELECT * FROM my_medicines WHERE name = ?", arrayOf(name))
            if (cursor.count > 0) {
                cursor.close()
                return false // Zaten eklenmiş
            }
            cursor.close()

            // Eklenmemişse ekle
            val values = ContentValues().apply {
                put("name", name)
            }

            val result = db.insert("my_medicines", null, values)
            return result != -1L
        }

    fun getAllMedicines(): List<String> {
        val medicineList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM my_medicines", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                medicineList.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return medicineList
    }

    fun deleteMedicine(name: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
        return result > 0
    }

}

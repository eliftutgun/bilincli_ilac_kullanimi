package com.egemenaydin.pharmacynew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyMedicinesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyMedicinesAdapter
    private lateinit var dbHelper: MyMedicinesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_medicines)

        recyclerView = findViewById(R.id.myMedicinesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = MyMedicinesDBHelper(this)

        // Sadece kullanıcı tarafından eklenen ilaç isimleri
        val myMedicinesList = dbHelper.getAllMedicines().toMutableList()
        adapter = MyMedicinesAdapter(myMedicinesList)
        recyclerView.adapter = adapter

    }
}

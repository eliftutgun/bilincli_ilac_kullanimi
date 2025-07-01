package com.egemenaydin.pharmacynew

import Medicine
import MedicineAdapter
import MedicineDBHelper
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MedicinesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var medicineAdapter: MedicineAdapter
    private lateinit var dbHelper: MedicineDBHelper
    private lateinit var searchView: SearchView

    private var medicinesList = ArrayList<Medicine>() // tüm ilaçlar burada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines)

        recyclerView = findViewById(R.id.medicineRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchView = findViewById(R.id.searchView)

        dbHelper = MedicineDBHelper(this)
        medicinesList = dbHelper.getAllMedicines() as ArrayList<Medicine>

        medicineAdapter = MedicineAdapter(medicinesList, this, dbHelper)
        recyclerView.adapter = medicineAdapter

        // Arama işlemi
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Arama butonuna basınca
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Yazarken anlık filtreleme
                filterList(newText)
                return true
            }
        })
    }



    private fun showMedicineDetails(medicine: Medicine) {
        val message = """
            • Etken Madde: ${medicine.etkenMadde}
            • Kullanım Amacı: ${medicine.usage}
            • Yan Etkiler: ${medicine.sideEffects}
            • Uyarılar: ${medicine.uyarilar}
        """.trimIndent()

        AlertDialog.Builder(this@MedicinesActivity)
            .setTitle(medicine.name)
            .setMessage(message)
            .setPositiveButton("Tamam", null)
            .show()
    }
    private fun filterList(text: String?) {
        val filteredList = ArrayList<Medicine>()

        if (text != null) {
            for (medicine in medicinesList) {
                if (medicine.name.contains(text, ignoreCase = true)) {
                    filteredList.add(medicine)
                }
            }
        }

        medicineAdapter.updateList(filteredList)
    }
}


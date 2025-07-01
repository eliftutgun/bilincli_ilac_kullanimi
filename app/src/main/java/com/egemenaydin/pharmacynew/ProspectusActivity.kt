package com.egemenaydin.pharmacynew

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProspectusActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProspectusAdapter
    private lateinit var dbHelper: ProspectusDBHelper
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private var prospectusList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prospectus)

        recyclerView = findViewById(R.id.prospectusRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchView = findViewById(R.id.searchViewProspectus)

        dbHelper = ProspectusDBHelper(this)

        // Listeyi al
        prospectusList = dbHelper.getAllProspectus() as ArrayList<String>

        adapter = ProspectusAdapter(prospectusList) { name ->
            showProspectus(name)
        }
        recyclerView.adapter = adapter

        // Arama dinleyicisi
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(text: String?) {
        val filteredList = ArrayList<String>()

        if (!text.isNullOrEmpty()) {
            for (name in prospectusList) {
                if (name.contains(text, ignoreCase = true)) {
                    filteredList.add(name)
                }
            }
        } else {
            filteredList.addAll(prospectusList)
        }

        adapter.updateList(filteredList)
    }

    private fun showProspectus(name: String) {
        val bilgi = dbHelper.getProspectusByName(name)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(name)
            .setMessage(bilgi)
            .setPositiveButton("Kapat", null)
            .create()
        dialog.show()
    }
}

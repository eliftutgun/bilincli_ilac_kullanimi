package com.egemenaydin.pharmacynew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyMedicinesAdapter(private val medicineList: MutableList<String>) :
    RecyclerView.Adapter<MyMedicinesAdapter.MyMedicineViewHolder>() {

    class MyMedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.myMedicineNameTextView)
        val removeButton: View = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_medicine, parent, false)
        return MyMedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyMedicineViewHolder, position: Int) {
        val medicineName = medicineList[position]
        holder.nameTextView.text = medicineName

        holder.removeButton.setOnClickListener {
            // Veritabanından sil
            val dbHelper = MyMedicinesDBHelper(holder.itemView.context)
            dbHelper.deleteMedicine(medicineName)

            // Listeden çıkar
            medicineList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, medicineList.size)
        }
    }

    override fun getItemCount(): Int = medicineList.size
}


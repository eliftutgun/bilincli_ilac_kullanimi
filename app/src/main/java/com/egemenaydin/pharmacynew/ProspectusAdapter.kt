package com.egemenaydin.pharmacynew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class ProspectusAdapter(private var prospectusList: List<String>, private val itemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<ProspectusAdapter.ProspectusViewHolder>() {

    class ProspectusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.medicineName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProspectusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prospectus_medicine, parent, false)
        return ProspectusViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProspectusViewHolder, position: Int) {
        val name = prospectusList[position]
        holder.nameTextView.text = name

        holder.itemView.setOnClickListener {
            itemClickListener(name)
        }
    }

    override fun getItemCount(): Int = prospectusList.size

    fun updateList(newList: List<String>) {
        prospectusList = newList
        notifyDataSetChanged()
    }
}

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.egemenaydin.pharmacynew.MyMedicinesDBHelper
import com.egemenaydin.pharmacynew.R

class MedicineAdapter(medicineList: List<Medicine>,
private val context: Context,
    private val dbHelper: MedicineDBHelper,):
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNameTextView: TextView = itemView.findViewById(R.id.medicineNameTextView)
    }
    private var medicineList = ArrayList(medicineList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = medicineList[position]
        holder.medicineNameTextView.text = medicine.name

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val message = """
                ➤ Etken Madde: ${medicine.etkenMadde}
                ➤ Kullanım Amacı: ${medicine.usage}
                ➤ Yan Etkiler: ${medicine.sideEffects}
                ➤ Uyarılar: ${medicine.uyarilar}
            """.trimIndent()

            AlertDialog.Builder(context)
                .setTitle(medicine.name)
                .setMessage(message)
                .setPositiveButton("Tamam", null)
                .show()
        }
        // Ekle butonu işlevi
        val addButton: Button = holder.itemView.findViewById(R.id.buttonAddToMyMedicines)
        addButton.setOnClickListener {
            val context = holder.itemView.context
            val dbHelper = MyMedicinesDBHelper(context)

            val success = dbHelper.addMedicine(medicine.name)

            val message = if (success) {
                "${medicine.name} ilaçlarınıza eklendi."
            } else {
                "${medicine.name} eklenemedi."
            }

            AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Tamam", null)
                .show()
        }




    }
    fun updateList(newList: List<Medicine>) {
        medicineList.clear()
        medicineList.addAll(newList)
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int = medicineList.size
}

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egemenaydin.pharmacynew.R
import com.egemenaydin.pharmacynew.Reminder

class ReminderAdapter(
    private var reminderList: List<Reminder>,
    private val onDeleteClick: (Reminder) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMedicine: TextView = itemView.findViewById(R.id.textMedicine)
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        val textRepeat: TextView = itemView.findViewById(R.id.textRepeat)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        // Burada hatırlatıcıyı alıyoruz
        val reminder = reminderList[position]

        // Log mesajı ekleyerek veriyi kontrol edebilirsiniz
        Log.d("ReminderAdapter", "Binding reminder: ${reminder.medicineName}")

        // Veriyi holder'lar ile bağlıyoruz
        holder.textMedicine.text = "İlaç: ${reminder.medicineName}"
        holder.textTime.text = "Saat: %02d:%02d".format(reminder.hour, reminder.minute)
        holder.textRepeat.text = if (reminder.repeat) "Her gün tekrarlanır" else "Tek seferlik"

        // Silme butonuna tıklandığında, delete işlemini çağırıyoruz
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(reminder)
        }
    }

    override fun getItemCount(): Int = reminderList.size

    // Yeni bir liste geldiğinde RecyclerView'i güncelliyoruz
    fun updateList(newList: List<Reminder>) {
        reminderList = newList
        notifyDataSetChanged()
    }
}


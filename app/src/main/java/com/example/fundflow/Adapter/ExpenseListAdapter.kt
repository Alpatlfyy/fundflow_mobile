import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.Domain.ExpenseDomain
import com.example.fundflow.R
import com.example.fundflow.databinding.ViewholderItemsBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListAdapter(
    private val items: MutableList<ExpenseDomain>,
    private val onEdit: (ExpenseDomain) -> Unit, // Callback untuk Edit
    private val onDelete: (ExpenseDomain) -> Unit // Callback untuk Hapus

) : RecyclerView.Adapter<ExpenseListAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderItemsBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    fun updateData(newItems: List<ExpenseDomain>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]

        // Set title and category
        holder.binding.titleTxt.text = item.kategori

        // Format date to readable string
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
        val formattedDate = dateFormat.format(item.tanggal.toDate())
        holder.binding.timeTxt.text = formattedDate

        // Format amount to Rupiah
        val priceText = "Rp ${DecimalFormat("#,###.00").format(item.jumlah)}"
        holder.binding.priceTxt.text = priceText

        // Set icon from drawable resource
        val iconResId = context.resources.getIdentifier(item.icon, "drawable", context.packageName)
        holder.binding.pic.setImageResource(iconResId)

        // Set text color based on type
        holder.binding.priceTxt.setTextColor(
            when (item.jenis.lowercase()) {
                "pengeluaran" -> Color.parseColor("#FF4747") // Red for expense
                "pemasukan" -> Color.parseColor("#00BC78")  // Green for income
                else -> Color.BLACK // Default for unknown types
            }
        )

        // Handle long click to show PopupMenu
        holder.itemView.setOnLongClickListener {
            val popupMenu = PopupMenu(context, holder.itemView)
            popupMenu.menuInflater.inflate(R.menu.item_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_option -> {
                        onEdit(item) // Trigger Edit callback
                        true
                    }
                    R.id.delete_option -> {
                        onDelete(item) // Trigger Delete callback
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int = items.size
}

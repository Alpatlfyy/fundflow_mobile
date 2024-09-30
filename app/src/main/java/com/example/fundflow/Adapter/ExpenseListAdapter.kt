import android.content.Context
import android.graphics.Color // Ubah di sini
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundflow.Domain.ExpenseDomain
import com.example.fundflow.Repository.MainRepository
import com.example.fundflow.databinding.ViewholderItemsBinding
import java.text.NumberFormat
import java.util.Locale

class ExpenseListAdapter(private val items: MutableList<ExpenseDomain>) :
    RecyclerView.Adapter<ExpenseListAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderItemsBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseListAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseListAdapter.Viewholder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.timeTxt.text = item.time

        // Format harga menjadi string dalam format Rupiah
        val priceText = MainRepository().getFormattedPrice(item.price, item.pic)
        holder.binding.priceTxt.text = priceText

        // Mengubah warna teks berdasarkan jenis
        holder.binding.priceTxt.setTextColor(
            if (item.pic == "img2") {
                Color.parseColor("#FF4747") // Warna untuk pengeluaran
            } else {
                Color.parseColor("#00BC78") // Warna default untuk pemasukan
            }
        )

        val drawableResourceId = holder.itemView.resources.getIdentifier(item.pic, "drawable", context.packageName)

        Glide.with(context)
            .load(drawableResourceId)
            .into(holder.binding.pic)
    }

    override fun getItemCount(): Int = items.size
}

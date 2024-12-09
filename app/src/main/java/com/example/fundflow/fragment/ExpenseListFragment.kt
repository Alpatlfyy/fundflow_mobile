package com.example.fundflow.fragment

import ExpenseListAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.Domain.ExpenseDomain
import com.example.fundflow.databinding.FragmentExpenseListBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ProgressBar
import com.example.fundflow.Activity.EditArusKasActivity
import com.example.fundflow.Activity.EditCategoriActivity

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ExpenseListAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        adapter = ExpenseListAdapter(
            mutableListOf(),
            onEdit = { expense ->
                val intent = Intent(context, EditArusKasActivity::class.java)
                intent.putExtra("DOCUMENT_ID", expense.id)
                intent.putExtra("CATEGORY_NAME", expense.kategori)
                intent.putExtra("CATEGORY_ICON", expense.icon)
                intent.putExtra("CATEGORY_TYPE", expense.jenis) // Pastikan ini sesuai dengan field di database
                intent.putExtra("CATEGORY_JENIS", expense.jenis) // Pemasukan atau Pengeluaran
                intent.putExtra("CATEGORY_NOMINAL", expense.jumlah)
                context?.startActivity(intent)

            },
            onDelete = { expense -> deleteExpenseFromFirestore(expense) }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Setup GestureDetector
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                // Ensure gesture detection is active
                return true // Return true to indicate that we are handling the event
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // If scrolling down
                if (distanceY < 0) {
                    // Trigger refresh
                    refreshData()
                    return true
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })

        // Set touch listener on the RecyclerView to detect pull gestures
        binding.recyclerView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

        // Load initial data
        fetchExpensesFromFirestore()
    }

    private fun refreshData() {
        // Menampilkan indikator loading
        binding.loadingIndicator.visibility = View.VISIBLE

        // Ambil data dari Firestore
        fetchExpensesFromFirestore {
            // Sembunyikan indikator loading setelah data selesai di-refresh
            binding.loadingIndicator.visibility = View.GONE
        }
    }


    private fun fetchExpensesFromFirestore(onComplete: (() -> Unit)? = null) {
        db.collection("aruskas")
            .orderBy("tanggal", com.google.firebase.firestore.Query.Direction.DESCENDING) // Urutkan dari terbaru
            .get()
            .addOnSuccessListener { documents ->
                val expenses = documents.map { document ->
                    val id = document.id
                    val icon = document.getString("icon") ?: ""
                    val jenis = document.getString("jenis") ?: ""
                    val jumlah = document.getDouble("jumlah") ?: 0.0
                    val kategori = document.getString("kategori") ?: ""
                    val tanggal = document.getTimestamp("tanggal") ?: com.google.firebase.Timestamp.now()

                    ExpenseDomain(
                        id = id,
                        icon = icon,
                        jenis = jenis,
                        jumlah = jumlah,
                        kategori = kategori,
                        tanggal = tanggal
                    )
                }
                adapter.updateData(expenses)
                onComplete?.invoke() // Memanggil callback untuk menyembunyikan indikator loading
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                onComplete?.invoke() // Tetap panggil callback meskipun gagal
            }
    }


    private fun deleteExpenseFromFirestore(expense: ExpenseDomain) {
        db.collection("aruskas")
            .document(expense.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                fetchExpensesFromFirestore() // Update after deletion
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditDialog(expense: ExpenseDomain) {
        // Show Edit dialog logic
        Toast.makeText(requireContext(), "Edit: ${expense.kategori}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

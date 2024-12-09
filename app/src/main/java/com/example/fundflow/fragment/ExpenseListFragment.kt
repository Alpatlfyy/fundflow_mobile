package com.example.fundflow.fragment

import ExpenseListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.Activity.EditArusKasActivity
import com.example.fundflow.Domain.ExpenseDomain
import com.example.fundflow.UserSingleton
import com.example.fundflow.databinding.FragmentExpenseListBinding
import com.google.firebase.firestore.FirebaseFirestore

class ExpenseListFragment : Fragment() {
    val currentUid = UserSingleton.getUid()
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ExpenseListAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var gestureDetector: GestureDetector
    private var lastRefreshTime: Long = 0 // Untuk debounce

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
                intent.putExtra("CATEGORY_TYPE", expense.jenis)
                intent.putExtra("CATEGORY_JENIS", expense.jenis)
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
                return true // Aktifkan deteksi gesture
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    val deltaY = e2.y - e1.y
                    if (deltaY > 100) { // Jika gesture scroll ke bawah signifikan
                        refreshData()
                        return true
                    }
                }
                return false
            }
        })

        // Pasang GestureDetector pada RecyclerView
        binding.recyclerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false // RecyclerView tetap dapat di-scroll
        }

        // Load initial data
        fetchExpensesFromFirestore()
    }

    private fun refreshData() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime < 1000) { // Batasi refresh tiap 2 detik
            return
        }
        lastRefreshTime = currentTime

        // Tampilkan indikator loading
        binding.loadingIndicator.visibility = View.VISIBLE

        // Ambil data dari Firestore
        fetchExpensesFromFirestore {
            // Sembunyikan indikator loading setelah selesai
            binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun fetchExpensesFromFirestore(onComplete: (() -> Unit)? = null) {

        db.collection("aruskas")
            .whereEqualTo("userid", currentUid)
            .orderBy("tanggal", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                if (!isAdded) return@addOnSuccessListener // Pastikan fragment masih aktif
                val expenses = documents.map { document ->
                    ExpenseDomain(
                        id = document.id,
                        icon = document.getString("icon") ?: "",
                        jenis = document.getString("jenis") ?: "",
                        jumlah = document.getDouble("jumlah") ?: 0.0,
                        kategori = document.getString("kategori") ?: "",
                        tanggal = document.getTimestamp("tanggal") ?: com.google.firebase.Timestamp.now()
                    )
                }
                adapter.updateData(expenses)
                onComplete?.invoke()
            }
            .addOnFailureListener { exception ->
                context?.let { ctx ->
                    Toast.makeText(ctx, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                }
                onComplete?.invoke()
            }
    }


    private fun deleteExpenseFromFirestore(expense: ExpenseDomain) {
        db.collection("aruskas")
            .document(expense.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                fetchExpensesFromFirestore() // Update setelah penghapusan
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.fundflow.Repository

import com.example.fundflow.Domain.ExpenseDomain
import java.text.DecimalFormat

class MainRepository {
    private val formatter = DecimalFormat("#,###.00") // Format untuk menampilkan angka

    val items = mutableListOf(
        ExpenseDomain("Jasa Konsultan", 57323.23, "img1", "4 April 2025"), // Pemasukan
        ExpenseDomain("Gaji Karyawan", 40019.19, "img2", "5 April 2025"),   // Pengeluaran
        ExpenseDomain("Jasa Lingkungan", 50010.10, "img1", "6 April 2025"), // Pemasukan
        ExpenseDomain("Gaji Karyawan", 40019.19, "img2", "10 April 2025"),  // Pengeluaran
    )

    fun getFormattedPrice(price: Double, pic: String): String {
        return if (pic == "img2") {
            "Rp -${formatter.format(price)}" // Format untuk pengeluaran
        } else {
            "Rp ${formatter.format(price)}" // Format untuk pemasukan
        }
    }
}

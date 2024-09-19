package com.example.fundflow.onboarding

import androidx.annotation.DrawableRes
import com.example.fundflow.R

sealed class OnBoardingModel (
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String,
    ) {
    data object FirstPages : OnBoardingModel(
        image = R.drawable.img_into_1,
        title = "Akses Tanpa\n" +
                "Batas",
        description = "Aplikasi kami tersedia kapan saja dan di mana saja. Dapat diakses 24/7 dari ponsel Anda, menjadikan transaksi keuangan lebih fleksibel."
        )
    data object SecondPages : OnBoardingModel(
        image = R.drawable.img_into_2,
        title = "Kelola Keuangan \n" +
                "Lebih Mudah",
        description = "Pantau pengeluaran dan pemasukan Anda dengan fitur arus kas yang kami sediakan. Lihat riwayat transaksi secara lengkap, atur anggaran bulanan."
    )
    data object ThirdPages : OnBoardingModel(
        image = R.drawable.img_into_3,
        title = "Buat Invoice dalam \n" +
                "Hitungan Detik",
        description = "Tidak perlu lagi repot menggunakan aplikasi terpisah untuk membuat tagihan. Dengan Fundflow, Anda dapat membuat dan mengirim invoice langsung dari ponsel Anda."
    )

}
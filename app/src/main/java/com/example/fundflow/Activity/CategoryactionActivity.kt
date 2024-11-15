package com.example.fundflow.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fundflow.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class CategoryactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val name = intent.getStringExtra("CATEGORY_NAME")
        val icon = intent.getIntExtra("CATEGORY_ICON", R.drawable.ic_def_rapat)
        val tabType = intent.getStringExtra("TAB_TYPE") // Retrieve the tab type ("Pemasukan" or "Pengeluaran")

        // Menampilkan BottomSheet saat Activity diluncurkan
        showBottomSheet(this, name, icon, tabType)
    }

    companion object {
        fun showBottomSheet(context: Context, name: String?, icon: Int?, tabType: String?) {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_action_category, null)
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTransparant)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            // Access LinearLayout from view and add OnClickListener
            val ubahNamaLayout = view.findViewById<LinearLayout>(R.id.ubahTitle)
            val hapusDaftarLayout = view.findViewById<LinearLayout>(R.id.hapusTitle)

            // Check the tab type and set it accordingly
            val type = tabType ?: "Pemasukan" // Default to "Pemasukan" if null

            ubahNamaLayout.setOnClickListener {
                val intent = Intent(context, EditCategoriActivity::class.java)
                (context as Activity).finish()
                intent.putExtra("CATEGORY_NAME", name)
                intent.putExtra("CATEGORY_ICON", icon)
                intent.putExtra("CATEGORY_TYPE", type)
                context.startActivity(intent)
            }

            hapusDaftarLayout.setOnClickListener {
                Toast.makeText(context, "Hapus dari Daftar diklik", Toast.LENGTH_SHORT).show()
            }

            // Show the BottomSheetDialog
            bottomSheetDialog.setOnShowListener {
                val bottomSheet =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    it.background =
                        ContextCompat.getDrawable(context, R.drawable.rounded_background)

                    val window = bottomSheetDialog.window
                    window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    window?.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.rounded_background
                        )
                    )
                    window?.setGravity(Gravity.BOTTOM)
                }
            }
        }
    }
}

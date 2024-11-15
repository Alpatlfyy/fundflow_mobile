package com.example.fundflow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundflow.Activity.TambahCategoryActivity
import com.example.fundflow.R

class KategoriFragmentKeluar(private val activity: TambahCategoryActivity) : Fragment() {

    private var selectedIconName: String? = null  // Menyimpan nama ikon yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category2, container, false)

        // Array ID ImageButton di layout
        val buttonIds = arrayOf(
            R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5, R.id.icon6,
            R.id.icon7, R.id.icon8, R.id.icon9, R.id.icon10, R.id.icon11, R.id.icon12,
            R.id.icon13, R.id.icon14, R.id.icon15, R.id.icon16, R.id.icon17, R.id.icon18,
            R.id.icon19, R.id.icon20, R.id.icon21, R.id.icon22, R.id.icon23, R.id.icon24,
            R.id.icon25, R.id.icon26, R.id.icon27, R.id.icon28, R.id.icon29, R.id.icon30,
            R.id.icon31, R.id.icon32, R.id.icon33, R.id.icon34, R.id.icon35, R.id.icon36,
            R.id.icon37, R.id.icon38, R.id.icon39, R.id.icon40, R.id.icon41, R.id.icon42,
            R.id.icon43, R.id.icon44, R.id.icon45, R.id.icon46, R.id.icon47, R.id.icon48,
            R.id.icon49, R.id.icon50, R.id.icon51, R.id.icon52, R.id.icon53, R.id.icon54,
            R.id.icon55, R.id.icon56, R.id.icon57, R.id.icon58, R.id.icon59, R.id.icon60
        )

        // Array nama file ikon untuk setiap ikon
        val iconNames = arrayOf(
            "ic_rapat", "ic_present", "ic_proyektor", "ic_helm", "ic_kerjsama", "ic_daun",
            "ic_dokumen", "ic_kacapembesar", "ic_catur", "ic_mic", "ic_perusahaan", "ic_rocket",
            "ic_lampu", "ic_book", "ic_tandatgn", "ic_catat", "ic_pidato", "ic_bumi", "ic_medali",
            "ic_lingkungan", "ic_mcd", "ic_kentang", "ic_ayam", "ic_rotitawar", "ic_mie", "ic_elur",
            "ic_nasi", "ic_kueultah", "ic_ikan", "ic_daging", "ic_gnja", "ic_apel", "ic_teh", "ic_es",
            "ic_kopi", "ic_botol", "ic_cup", "ic_shope", "ic_gojek", "ic_grab", "ic_flesdis", "ic_kursi",
            "ic_pos", "ic_komputer", "ic_pin", "ic_lembaran", "ic_kertaspnjg", "ic_peniti", "ic_telpun",
            "ic_print", "ic_mobil", "ic_sepeda", "ic_bus", "ic_helykopter", "ic_truk", "ic_motor",
            "ic_kereta", "ic_kapal", "ic_jet", "ic_pesawat"
        )

        // Set listeners for each button
        buttonIds.forEachIndexed { index, buttonId ->
            val button: ImageButton = view.findViewById(buttonId)
            button.setOnClickListener {
                selectedIconName = iconNames[index]
                // Update the selected icon in the activity
                updateIcon()
            }
        }

        return view
    }

    private fun updateIcon() {
        val iconResourceId = resources.getIdentifier(selectedIconName, "drawable", activity.packageName)
        activity.changeImage(iconResourceId) // Update icon on the activity
        Toast.makeText(activity, "Ikon dipilih: $selectedIconName", Toast.LENGTH_SHORT).show()
    }
}

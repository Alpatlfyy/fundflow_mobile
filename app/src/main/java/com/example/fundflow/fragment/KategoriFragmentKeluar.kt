package com.example.fundflow.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.fundflow.Activity.TambahCategoryActivity
import com.example.fundflow.R

class KategoriFragmentKeluar(private val activity: TambahCategoryActivity) : Fragment() {
    @SuppressLint("MissingInflatedId")
    private lateinit var selectedBulatan: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category2, container, false)

        // Inisialisasi ImageButton
        val button1: ImageButton = view.findViewById(R.id.icon1) // Ganti dengan ID yang sesuai
        val button2: ImageButton = view.findViewById(R.id.icon2)
        val button3: ImageButton = view.findViewById(R.id.icon3)
        val button4: ImageButton = view.findViewById(R.id.icon4)
        val button5: ImageButton = view.findViewById(R.id.icon5)
        val button6: ImageButton = view.findViewById(R.id.icon6)
        val button7: ImageButton = view.findViewById(R.id.icon7)
        val button8: ImageButton = view.findViewById(R.id.icon8)
        val button9: ImageButton = view.findViewById(R.id.icon9)
        val button10: ImageButton = view.findViewById(R.id.icon10)
        val button11: ImageButton = view.findViewById(R.id.icon11)
        val button12: ImageButton = view.findViewById(R.id.icon12)
        val button13: ImageButton = view.findViewById(R.id.icon13)
        val button14: ImageButton = view.findViewById(R.id.icon14)
        val button15: ImageButton = view.findViewById(R.id.icon15)
        val button16: ImageButton = view.findViewById(R.id.icon16)
        val button17: ImageButton = view.findViewById(R.id.icon17)
        val button18: ImageButton = view.findViewById(R.id.icon18)
        val button19: ImageButton = view.findViewById(R.id.icon19)
        val button20: ImageButton = view.findViewById(R.id.icon20)
        val button21: ImageButton = view.findViewById(R.id.icon21)
        val button22: ImageButton = view.findViewById(R.id.icon22)
        val button23: ImageButton = view.findViewById(R.id.icon23)
        val button24: ImageButton = view.findViewById(R.id.icon24)
        val button25: ImageButton = view.findViewById(R.id.icon25)
        val button26: ImageButton = view.findViewById(R.id.icon26)
        val button27: ImageButton = view.findViewById(R.id.icon27)
        val button28: ImageButton = view.findViewById(R.id.icon28)
        val button29: ImageButton = view.findViewById(R.id.icon29)
        val button30: ImageButton = view.findViewById(R.id.icon30)
        val button31: ImageButton = view.findViewById(R.id.icon31)
        val button32: ImageButton = view.findViewById(R.id.icon32)
        val button33: ImageButton = view.findViewById(R.id.icon33)
        val button34: ImageButton = view.findViewById(R.id.icon34)
        val button35: ImageButton = view.findViewById(R.id.icon35)
        val button36: ImageButton = view.findViewById(R.id.icon36)
        val button37: ImageButton = view.findViewById(R.id.icon37)
        val button38: ImageButton = view.findViewById(R.id.icon38)
        val button39: ImageButton = view.findViewById(R.id.icon39)
        val button40: ImageButton = view.findViewById(R.id.icon40)
        val button41: ImageButton = view.findViewById(R.id.icon41)
        val button42: ImageButton = view.findViewById(R.id.icon42)
        val button43: ImageButton = view.findViewById(R.id.icon43)
        val button44: ImageButton = view.findViewById(R.id.icon44)
        val button45: ImageButton = view.findViewById(R.id.icon45)
        val button46: ImageButton = view.findViewById(R.id.icon46)
        val button47: ImageButton = view.findViewById(R.id.icon47)
        val button48: ImageButton = view.findViewById(R.id.icon48)
        val button49: ImageButton = view.findViewById(R.id.icon49)
        val button50: ImageButton = view.findViewById(R.id.icon50)
        val button51: ImageButton = view.findViewById(R.id.icon51)
        val button52: ImageButton = view.findViewById(R.id.icon52)
        val button53: ImageButton = view.findViewById(R.id.icon53)
        val button54: ImageButton = view.findViewById(R.id.icon54)
        val button55: ImageButton = view.findViewById(R.id.icon55)
        val button56: ImageButton = view.findViewById(R.id.icon56)
        val button57: ImageButton = view.findViewById(R.id.icon57)
        val button58: ImageButton = view.findViewById(R.id.icon58)
        val button59: ImageButton = view.findViewById(R.id.icon59)
        val button60: ImageButton = view.findViewById(R.id.icon60)



        // Set listener untuk masing-masing ImageButton
        // Set listener untuk masing-masing ImageButton
        button1.setOnClickListener {
            activity.changeImage(R.drawable.ic_rapat)
        }

        button2.setOnClickListener {
            activity.changeImage(R.drawable.ic_present)
        }

        button3.setOnClickListener {
            activity.changeImage(R.drawable.ic_proyektor)
        }

        button4.setOnClickListener {
            activity.changeImage(R.drawable.ic_helm)
        }

        button5.setOnClickListener {
            activity.changeImage(R.drawable.ic_kerjsama)
        }

        button6.setOnClickListener {
            activity.changeImage(R.drawable.ic_daun)
        }

        button7.setOnClickListener {
            activity.changeImage(R.drawable.ic_dokumen)
        }

        button8.setOnClickListener {
            activity.changeImage(R.drawable.ic_kacapembesar)
        }

        button9.setOnClickListener {
            activity.changeImage(R.drawable.ic_catur)
        }

        button10.setOnClickListener {
            activity.changeImage(R.drawable.ic_mic)
        }

        button11.setOnClickListener {
            activity.changeImage(R.drawable.ic_perusahaan)
        }

        button12.setOnClickListener {
            activity.changeImage(R.drawable.ic_rocket)
        }

        button13.setOnClickListener {
            activity.changeImage(R.drawable.ic_lampu)
        }

        button14.setOnClickListener {
            activity.changeImage(R.drawable.ic_book)
        }

        button15.setOnClickListener {
            activity.changeImage(R.drawable.ic_tandatgn)
        }

        button16.setOnClickListener {
            activity.changeImage(R.drawable.ic_catat)
        }

        button17.setOnClickListener {
            activity.changeImage(R.drawable.ic_pidato)
        }

        button18.setOnClickListener {
            activity.changeImage(R.drawable.ic_bumi)
        }

        button19.setOnClickListener {
            activity.changeImage(R.drawable.ic_medali)
        }

        button20.setOnClickListener {
            activity.changeImage(R.drawable.ic_lingkungan)
        }

        button21.setOnClickListener {
            activity.changeImage(R.drawable.ic_mcd)
        }

        button22.setOnClickListener {
            activity.changeImage(R.drawable.ic_kentang)
        }

        button23.setOnClickListener {
            activity.changeImage(R.drawable.ic_ayam)
        }

        button24.setOnClickListener {
            activity.changeImage(R.drawable.ic_rotitawar)
        }

        button25.setOnClickListener {
            activity.changeImage(R.drawable.ic_mie)
        }

        button26.setOnClickListener {
            activity.changeImage(R.drawable.ic_elur)
        }

        button27.setOnClickListener {
            activity.changeImage(R.drawable.ic_nasi)
        }

        button28.setOnClickListener {
            activity.changeImage(R.drawable.ic_kueultah)
        }

        button29.setOnClickListener {
            activity.changeImage(R.drawable.ic_ikan)
        }

        button30.setOnClickListener {
            activity.changeImage(R.drawable.ic_daging)
        }

        button31.setOnClickListener {
            activity.changeImage(R.drawable.ic_gnja)
        }

        button32.setOnClickListener {
            activity.changeImage(R.drawable.ic_apel)
        }

        button33.setOnClickListener {
            activity.changeImage(R.drawable.ic_teh)
        }

        button34.setOnClickListener {
            activity.changeImage(R.drawable.ic_es)
        }

        button35.setOnClickListener {
            activity.changeImage(R.drawable.ic_kopi)
        }

        button36.setOnClickListener {
            activity.changeImage(R.drawable.ic_botol)
        }

        button37.setOnClickListener {
            activity.changeImage(R.drawable.ic_cup)
        }

        button38.setOnClickListener {
            activity.changeImage(R.drawable.ic_shope)
        }

        button39.setOnClickListener {
            activity.changeImage(R.drawable.ic_gojek)
        }

        button40.setOnClickListener {
            activity.changeImage(R.drawable.ic_grab)
        }

        button41.setOnClickListener {
            activity.changeImage(R.drawable.ic_flesdis)
        }

        button42.setOnClickListener {
            activity.changeImage(R.drawable.ic_kursi)
        }

        button43.setOnClickListener {
            activity.changeImage(R.drawable.ic_pos)
        }

        button44.setOnClickListener {
            activity.changeImage(R.drawable.ic_komputer)
        }

        button45.setOnClickListener {
            activity.changeImage(R.drawable.ic_pin)
        }

        button46.setOnClickListener {
            activity.changeImage(R.drawable.ic_lembaran)
        }

        button47.setOnClickListener {
            activity.changeImage(R.drawable.ic_kertaspnjg)
        }

        button48.setOnClickListener {
            activity.changeImage(R.drawable.ic_peniti)
        }

        button49.setOnClickListener {
            activity.changeImage(R.drawable.ic_telpun)
        }

        button50.setOnClickListener {
            activity.changeImage(R.drawable.ic_print)
        }

        button51.setOnClickListener {
            activity.changeImage(R.drawable.ic_mobil)
        }

        button52.setOnClickListener {
            activity.changeImage(R.drawable.ic_sepeda)
        }

        button53.setOnClickListener {
            activity.changeImage(R.drawable.ic_bus)
        }

        button54.setOnClickListener {
            activity.changeImage(R.drawable.ic_helykopter)
        }

        button55.setOnClickListener {
            activity.changeImage(R.drawable.ic_truk)
        }

        button56.setOnClickListener {
            activity.changeImage(R.drawable.ic_motor)
        }

        button57.setOnClickListener {
            activity.changeImage(R.drawable.ic_kereta)
        }

        button58.setOnClickListener {
            activity.changeImage(R.drawable.ic_kapal)
        }

        button59.setOnClickListener {
            activity.changeImage(R.drawable.ic_jet)
        }

        button60.setOnClickListener {
            activity.changeImage(R.drawable.ic_pesawat
            )
        }


        return view
    }
}


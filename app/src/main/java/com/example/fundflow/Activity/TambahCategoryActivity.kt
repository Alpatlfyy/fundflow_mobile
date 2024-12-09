package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fundflow.Adapter.RecentOperationsAdapter

import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.example.fundflow.fragment.KategoriFragmentKeluar
import com.example.fundflow.fragment.KategoriFragmentMasuk
import com.example.fundflow.model.RecentOperation
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class TambahCategoryActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var nameCategoryEditText: EditText
    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var imageViewBulat: ImageView
    private var selectedIconResourceId: Int = R.drawable.ic_def_rapat // Default icon
    private lateinit var recentOperationsList: MutableList<RecentOperation>
    private lateinit var recentOperationsAdapter: RecentOperationsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_category)

        recentOperationsList = mutableListOf()
        recentOperationsAdapter = RecentOperationsAdapter(recentOperationsList)


        // Initialize UI components
        setupUI()
        displayFragment(0)
        setupTabListener()
        setupAddCategoryButton()
    }

    private fun setupUI() {
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        imageViewBulat = findViewById(R.id.iconKategori)
        nameCategoryEditText = findViewById(R.id.namaCategory)

        // Setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Back button listener
        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener {
            onBackPressed()
        }

        // Setup TabLayout
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        fragmentContainer = findViewById(R.id.fragmentContainer)

        // Icon selection dialog listener
        imageViewBulat.setOnClickListener {
            showIconSelectionDialog()
        }
    }

    private fun showIconSelectionDialog() {
        val iconList = arrayOf(
            R.drawable.ic_rapat,
            R.drawable.ic_present,
            R.drawable.ic_proyektor,
            R.drawable.ic_helm,
            R.drawable.ic_kerjsama,
            R.drawable.ic_daun,
            R.drawable.ic_dokumen,
            R.drawable.ic_kacapembesar,
            R.drawable.ic_catur,
            R.drawable.ic_mic,
            R.drawable.ic_perusahaan,
            R.drawable.ic_rocket,
            R.drawable.ic_lampu,
            R.drawable.ic_book,
            R.drawable.ic_tandatgn,
            R.drawable.ic_catat,
            R.drawable.ic_pidato,
            R.drawable.ic_bumi,
            R.drawable.ic_medali,
            R.drawable.ic_lingkungan,
            R.drawable.ic_mcd,
            R.drawable.ic_kentang,
            R.drawable.ic_ayam,
            R.drawable.ic_rotitawar,
            R.drawable.ic_mie,
            R.drawable.ic_elur,
            R.drawable.ic_nasi,
            R.drawable.ic_kueultah,
            R.drawable.ic_ikan,
            R.drawable.ic_daging,
            R.drawable.ic_gnja,
            R.drawable.ic_apel,
            R.drawable.ic_teh,
            R.drawable.ic_es,
            R.drawable.ic_kopi,
            R.drawable.ic_botol,
            R.drawable.ic_cup,
            R.drawable.ic_shope,
            R.drawable.ic_gojek,
            R.drawable.ic_grab,
            R.drawable.ic_flesdis,
            R.drawable.ic_kursi,
            R.drawable.ic_pos,
            R.drawable.ic_komputer,
            R.drawable.ic_pin,
            R.drawable.ic_lembaran,
            R.drawable.ic_kertaspnjg,
            R.drawable.ic_peniti,
            R.drawable.ic_telpun,
            R.drawable.ic_print,
            R.drawable.ic_mobil,
            R.drawable.ic_sepeda,
            R.drawable.ic_bus,
            R.drawable.ic_helykopter,
            R.drawable.ic_truk,
            R.drawable.ic_motor,
            R.drawable.ic_kereta,
            R.drawable.ic_kapal,
            R.drawable.ic_jet,
            R.drawable.ic_pesawat
        ) // List of icons
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
        ) // Icon names for dialog

        AlertDialog.Builder(this)
            .setTitle("Pilih Ikon Kategori")
            .setItems(iconNames) { _, which ->
                selectedIconResourceId = iconList[which]
                imageViewBulat.setImageResource(selectedIconResourceId)
                Toast.makeText(this, "Ikon diperbarui", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun setupTabListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                displayFragment(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupAddCategoryButton() {
        findViewById<Button>(R.id.btnTambahKategori)?.setOnClickListener {
            val categoryName = getCategoryName()
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Nama kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoryType = if (tabLayout.selectedTabPosition == 0) "Pemasukan" else "Pengeluaran"
            checkAndAddCategory(categoryName, categoryType, selectedIconResourceId)
        }
    }

    private fun displayFragment(position: Int) {
        val fragment = when (position) {
            0 -> KategoriFragmentMasuk(this)
            1 -> KategoriFragmentKeluar(this)
            else -> null
        }
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }
    }

    fun getCategoryName(): String {
        return nameCategoryEditText.text.toString().trim()
    }
    fun changeImage(iconResourceId: Int) {
        selectedIconResourceId = iconResourceId
        imageViewBulat.setImageResource(selectedIconResourceId)
    }

    private fun checkAndAddCategory(
        categoryName: String,
        categoryType: String,
        iconResourceId: Int
    ) {
        val currentUid = UserSingleton.getUid()
        db.collection("kategori")
            .whereEqualTo("name", categoryName)
            .whereEqualTo("type", categoryType)
            .whereEqualTo("userid",currentUid)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    addCategoryToFirestore(categoryName, categoryType, iconResourceId, userid = currentUid)
                } else {
                    Toast.makeText(this, "Kategori sudah ada", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memeriksa kategori: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun addCategoryToFirestore(
        categoryName: String,
        categoryType: String,
        iconResourceId: Int,
        userid: String
    ) {
        val iconName = getDrawableName(iconResourceId)
        val category = hashMapOf(
            "name" to categoryName,
            "type" to categoryType,
            "icon" to iconName,
            "userid" to userid
        )

        db.collection("kategori")
            .add(category)
            .addOnSuccessListener {
                // Tambahkan kategori ke list operasi terbaru
                val newCategory = RecentOperation(
                    type = "Kategori",
                    name = categoryName,
                    icon = iconResourceId

                )
                recentOperationsList.add(0, newCategory) // Tambahkan ke atas
                recentOperationsAdapter.notifyItemInserted(0)

                // Navigasi kembali ke CategoryActivity
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }


    private fun getDrawableName(drawableId: Int): String {
        return resources.getResourceEntryName(drawableId)
    }
}

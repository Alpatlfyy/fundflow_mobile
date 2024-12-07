package com.example.fundflow.model
data class AnggotaItem(
    val id: String = "",
    val nama: String = "",
    val alamat: String = "",
    val noTelepon: String = "",
    val tanggalLahir: String = "",
    val jabatan: String = "",
    val jenisKelamin: String = ""
) {
    companion object {
        fun fromMap(data: Map<String, Any>, id: String): AnggotaItem {
            return AnggotaItem(
                id = id,
                nama = data["nama"] as? String ?: "",
                alamat = data["alamat"] as? String ?: "",
                noTelepon = data["noTelepon"] as? String ?: "",
                tanggalLahir = data["tanggalLahir"] as? String ?: "",
                jabatan = data["jabatan"] as? String ?: "",
                jenisKelamin = data["jenisKelamin"] as? String ?: ""
            )
        }
    }
}


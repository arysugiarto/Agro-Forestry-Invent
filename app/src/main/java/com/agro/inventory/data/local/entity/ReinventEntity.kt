package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.REINVENT)
data class ReinventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idPlot: Int? = null,
    val kodePlot: String? = null,
    val jarakTanam: String? = null,
    val reinventPhase: Int? = null,
    val polaTanam: String? = null,
    val comodity: String? = null,
    val jmlTanam: String? = null,
    val jmlHidup: String? = null,
    val jmlSakit: String? = null,
    val jmlMati: String? = null,
    val keliling: String? = null,
    val tinggi: String? = null,
    val edit: Boolean? = false,
    val penyulaman: String? = null,
    val photo: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val idComodity: String? = null,
    val plantNumber: String? = null,
)



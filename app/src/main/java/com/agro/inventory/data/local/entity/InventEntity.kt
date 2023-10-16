package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const
import com.google.gson.annotations.SerializedName

@Entity(tableName = Const.Database.Table.INVENT)
data class InventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idPlot: Int? = null,
    val kodePlot: String? = null,
    val jarakTanam: String? = null,
    val polaTanam: String? = null,
    val comodity: String? = null,
    val idComodity: String? = null,
    val jmlTanam: String? = null,
    val keliling: String? = null,
    val tinggi: String? = null,
    val edit: Boolean? = false,
    val photo: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val status: Boolean? = null
)



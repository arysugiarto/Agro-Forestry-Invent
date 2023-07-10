package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.INVENT_DATA)
data class InventDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val komoditas: String? = null,
    val plotId: Int? = null,
    val plantNumber: String? = null,
    val diesTotal: Int? = null,
    val alivesTotal: Int? = null,
    val diseasedTrees: Int? = null,
    val penyulamanTotal: String? = null,
    val totalPlant: Int? = null,
    val komoditasId: Int? = null,
    val photoAerial: String? = null,
    val photo: String? = null,
    val diameter: Double? = null,
    val diameterManual: String? = null,
    val keliling: String? = null,
    val kelilingManual: String? = null,
    val length: Int? = null,
    val lengthManual: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val userId: Int? = null,
    val uid: String? = null,
    val notes: String? = null,
    val countReinvent: String? = null,
    val kodePlot : String? = null
)



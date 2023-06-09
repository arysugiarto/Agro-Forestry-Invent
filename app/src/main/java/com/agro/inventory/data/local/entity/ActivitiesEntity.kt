package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.ACTIVITIES)
data class ActivitiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idPlot: Int? = null,
    val kodePlot: String? = null,
    val workersId: String? = null,
    val polaTanam: String? = null,
    val satuan: String? =null,
    val comodity: String? = null,
    val pekerja: String? = null,
    val nameActivity: String? = null,
    val pekerjaanId: Int? = null,
    val namaPekerjaan: String? = null,
    val activityId: Int? = null,
    val volume: String? = null,
    val photo: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val uid: String? = null,
    val appSource: String? = null,
    val createdBy: Int? = null,
    val deleted: Int? = null
)



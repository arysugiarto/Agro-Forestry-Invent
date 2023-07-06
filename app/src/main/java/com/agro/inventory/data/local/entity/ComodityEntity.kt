package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.COMODITY)
data class ComodityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idPlot: Int? = null,
    val kodePlot: String? = null,
    val comodity: String? = null,
    val idComodity: String? = null,
)



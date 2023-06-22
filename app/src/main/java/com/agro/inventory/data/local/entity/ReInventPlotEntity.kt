package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.PLOT_REINVENT)
data class ReInventPlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idPlot: Int? = null,
    val kodePlot: String? = null,
    val namearea: String? = null,
    val nameMember: String? = null,
    val memberNo: String? = null,
    val komoditas: String? = null,
    val polaTanam: String? = null,
    val status: Boolean? = null,
    val statusDone: Boolean? = null
)



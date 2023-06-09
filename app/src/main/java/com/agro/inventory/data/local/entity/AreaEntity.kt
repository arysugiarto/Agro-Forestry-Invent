package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.AREA)
data class AreaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val areaId: Int? = null,
    val memberno: String? = null,
    val namearea: String? = null,
    val nameMember: String? = null,
    val plotCount: String? = null,
    val status: Boolean? = null,
    val statusDone: Boolean? = null
)



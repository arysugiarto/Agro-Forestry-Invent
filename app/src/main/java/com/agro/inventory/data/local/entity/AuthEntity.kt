package com.agro.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agro.inventory.util.Const

@Entity(tableName = Const.Database.Table.AUTH)
data class AuthEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userAccessId: Int? = null,
    val username: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val roleTypeId: String? = null,
    val primaryDeviceId: String? = null,

)



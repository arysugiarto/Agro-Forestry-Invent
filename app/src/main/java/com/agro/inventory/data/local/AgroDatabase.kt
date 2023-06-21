package com.agro.inventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agro.inventory.data.local.dao.InvenPlotDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity

@Database(
    entities = [InventPlotEntity::class, InventEntity::class, ReinventEntity::class],
    version = 31,
    exportSchema = false
)
abstract class AgroDatabase : RoomDatabase() {
    abstract fun inventPlotDao(): InvenPlotDao

    abstract fun inventDao(): InventDao
    abstract fun reInventDao(): ReInventDao

}
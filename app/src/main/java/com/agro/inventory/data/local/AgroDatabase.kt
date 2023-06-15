package com.agro.inventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agro.inventory.data.local.dao.ActivitiesDao
import com.agro.inventory.data.local.dao.AreaDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity

@Database(
    entities = [ActivitiesEntity::class, AreaEntity::class, InventEntity::class, ReinventEntity::class],
    version = 26,
    exportSchema = false
)
abstract class AgroDatabase : RoomDatabase() {
    abstract fun activitiesDao(): ActivitiesDao
    abstract fun areaDao(): AreaDao

    abstract fun inventDao(): InventDao
    abstract fun reInventDao(): ReInventDao

}
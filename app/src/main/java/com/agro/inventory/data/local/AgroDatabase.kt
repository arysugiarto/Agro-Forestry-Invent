package com.agro.inventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agro.inventory.data.local.dao.ActivitiesDao
import com.agro.inventory.data.local.dao.AreaDao
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity

@Database(
    entities = [ActivitiesEntity::class, AreaEntity::class],
    version = 23,
    exportSchema = false
)
abstract class AgroDatabase : RoomDatabase() {
    abstract fun activitiesDao(): ActivitiesDao
    abstract fun areaDao(): AreaDao

}
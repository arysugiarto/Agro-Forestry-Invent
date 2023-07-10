package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface InventDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventData(comodityEntity: List<InventDataEntity>)

    @Query("SELECT * FROM invent_data_entity WHERE kodePlot =:kodePlot")
     fun getInventData(kodePlot: String): Flow<List<InventDataEntity>>


    @Query("DELETE FROM invent_data_entity")
    suspend fun deleteInventData()

}
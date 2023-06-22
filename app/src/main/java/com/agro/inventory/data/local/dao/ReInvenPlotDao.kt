package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ReInvenPlotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlotReInvent(reInventPlotEntity: List<ReInventPlotEntity>)

    @Query("SELECT * FROM plot_reinvent_entity")
    fun getPlotReInvent(): Flow<List<ReInventPlotEntity>>

    @Query("UPDATE plot_reinvent_entity SET status =:status, statusDone =:statusDone WHERE memberno =:memberno")
    suspend fun updateStatusPlotReInvent(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?
    )

    @Query("DELETE FROM plot_reinvent_entity")
    suspend fun deletePlotReInvent()

}
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

    @Query("SELECT * FROM plot_reinvent_entity WHERE " +
            "kodePlot LIKE :queryString OR allData LIKE :queryString " + "ORDER BY id ASC")
    fun getPlotReInvent(queryString: String): Flow<List<ReInventPlotEntity>>

    @Query("UPDATE plot_reinvent_entity SET  status =:status, statusDone =:statusDone WHERE kodePlot =:kodePlot")
    suspend fun updateStatusPlotReInvent(
        status:Boolean?,
        statusDone: Boolean?,
        kodePlot: String?,

    )

    @Query("DELETE FROM plot_reinvent_entity")
    suspend fun deletePlotReInvent()

    @Query("DELETE FROM plot_reinvent_entity WHERE id = :id")
    suspend fun deleteItemReInventPlotId(id: Int?)

}
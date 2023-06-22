package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.InventPlotEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface InvenPlotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlotInvent(inventPlotEntity: List<InventPlotEntity>)

//    @Query("SELECT * FROM plot_invent_entity WHERE " +
//            "kodePlot LIKE :queryString" + ORDER BY id ASC")
//    fun getPlotInvent(kodePlot : String?): Flow<List<InventPlotEntity>>
//
        @Query("SELECT * FROM plot_invent_entity WHERE " +
                "kodePlot LIKE :queryString OR allData LIKE :queryString " + "ORDER BY id ASC")
        fun getPlotInvent(queryString: String): Flow<List<InventPlotEntity>>

    @Query("UPDATE plot_invent_entity SET status =:status, statusDone =:statusDone WHERE memberno =:memberno")
    suspend fun updateStatusPlotInvent(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?
    )

    @Query("DELETE FROM plot_invent_entity")
    suspend fun deletePlotInvent()

}
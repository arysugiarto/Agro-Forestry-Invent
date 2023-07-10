package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ComodityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComodity(comodityEntity: List<ComodityEntity>)

    @Query("SELECT * FROM comodity_entity WHERE kodePlot =:kodePlot")
     fun getComodity(kodePlot: String): Flow<List<ComodityEntity>>

    @Query("UPDATE comodity_entity SET status =:status WHERE kodePlot =:kodePlot AND comodity =:comodity")
    suspend fun updateStatusComodity(
        status:Boolean?,
        kodePlot: String?,
        comodity:String?
    )

    @Query("DELETE FROM comodity_entity")
    suspend fun deleteComodity()

}
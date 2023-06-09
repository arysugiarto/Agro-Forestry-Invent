package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.AreaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(areaEntity: List<AreaEntity>)

    @Query("SELECT * FROM AREA_ENTITY")
    fun getArea(): Flow<List<AreaEntity>>

    @Query("UPDATE AREA_ENTITY SET status =:status, statusDone =:statusDone WHERE memberno =:memberno")
    suspend fun updateStatusArea(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?
    )

    @Query("DELETE FROM AREA_ENTITY")
    suspend fun deleteArea()

}
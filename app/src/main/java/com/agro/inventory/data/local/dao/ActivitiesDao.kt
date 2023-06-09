package com.agro.inventory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agro.inventory.data.local.entity.ActivitiesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ActivitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: ActivitiesEntity)

    @Query("SELECT * FROM ACTIVITIES_ENTITY")
    fun getActivitiesAll(): Flow<List<ActivitiesEntity>>

    @Query("SELECT * FROM ACTIVITIES_ENTITY WHERE kodePlot = :idPlot OR pekerjaanId = :pekerjaanId")
     fun getActivities(idPlot: String, pekerjaanId: String?): Flow<List<ActivitiesEntity>>

    @Query("SELECT * FROM ACTIVITIES_ENTITY WHERE kodePlot = :idPlot AND pekerjaanId = :pekerjaanId")
    fun getActivitiesByIdJob(idPlot: String, pekerjaanId: String?): Flow<List<ActivitiesEntity>>


    @Query("UPDATE ACTIVITIES_ENTITY SET pekerja =:pekerja, namaPekerjaan =:namaPekerjaan, pekerjaanId =:idPekerjaan, nameActivity =:nameActivity, lat =:lat, lng =:lng, volume =:volume, satuan =:satuan, photo =:photo, workersId =:workersId WHERE id =:id")
    suspend fun updateActivities(
        pekerja: String?,
        namaPekerjaan: String?,
        idPekerjaan: String?,
        nameActivity: String?,
        lat: String?,
        lng: String?,
        volume: String?,
        satuan: String?,
        photo: String?,
        workersId: String?,
        id: String?
    )

    @Query("DELETE FROM ACTIVITIES_ENTITY")
    suspend fun deleteActivities()

    @Query("DELETE FROM ACTIVITIES_ENTITY WHERE id = :id")
    suspend fun deleteActivitiesId(id: Int?)


}
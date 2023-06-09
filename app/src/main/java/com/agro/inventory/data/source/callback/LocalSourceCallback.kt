package com.agro.inventory.data.source.callback

import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import kotlinx.coroutines.flow.Flow

interface LocalSourceCallback {

    suspend fun insertActivitiesLocal(activitiesEntity: ActivitiesEntity)

    fun getLocalActivitiesAll(): Flow<List<ActivitiesEntity>>
     fun getLocalActivities(idPlot: String,pekerjaanId: String): Flow<List<ActivitiesEntity>>

    fun getLocalActivitiesByIdJob(idPlot: String,pekerjaanId: String): Flow<List<ActivitiesEntity>>

    suspend fun updateActivities(
        pekerja: String?,
        namaPekerjaan:String?,
        idPekerjaan: String?,
        nameActivity: String?,
        lat: String?,
        lng: String?,
        volume: String?,
        satuan: String?,
        photo: String?,
        workerId: String?,
        id: String?
    )


    suspend fun insertAreaLocal(areaEntity: List<AreaEntity>)

    fun getLocalArea(): Flow<List<AreaEntity>>

    suspend fun updateStatusArea(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?

    )

    suspend fun deleteActivities()

    suspend fun deleteArea()

    suspend fun deleteLocalItemActivities(idActivities: Int?)

}
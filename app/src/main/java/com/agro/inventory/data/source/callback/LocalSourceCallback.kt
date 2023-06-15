package com.agro.inventory.data.source.callback

import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import kotlinx.coroutines.flow.Flow

interface LocalSourceCallback {


    fun getLocalActivitiesAll(): Flow<List<ActivitiesEntity>>
     fun getLocalActivities(idPlot: String,pekerjaanId: String): Flow<List<ActivitiesEntity>>

    fun getLocalActivitiesByIdJob(idPlot: String,pekerjaanId: String): Flow<List<ActivitiesEntity>>

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

    suspend fun insertReInventLocal(reinventEntity: ReinventEntity)

    fun getReInvent(idComodity: String): Flow<List<ReinventEntity>>

    suspend fun updateReInvent(
        jmlTanam: String?,
        jmlHidup: String?,
        jmlSakit: String?,
        keliling: String?,
        tinggi: String?,
        photo: String?,
        lat: String?,
        lng: String?,
        idComodity: Int?,
        id: String?
    )

    suspend fun insertInventLocal(inventEntity: InventEntity)

    fun getInvent(idComodity: String): Flow<List<InventEntity>>

    suspend fun updateInvent(
        jmlTanam: String?,
        keliling: String?,
        tinggi: String?,
        idComodity: Int?,
        photo: String?,
        lat: String?,
        lng: String?,
        id: String?
    )


}
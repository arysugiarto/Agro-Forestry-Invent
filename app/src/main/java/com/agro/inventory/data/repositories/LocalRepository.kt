package com.agro.inventory.data.repositories

import com.agro.inventory.data.source.callback.LocalSourceCallback
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.source.data.LocalDataSource


class LocalRepository(
    localDataSource: LocalDataSource
) : LocalSourceCallback {
    private val localDataSource = localDataSource

    override suspend fun insertActivitiesLocal(activitiesEntity: ActivitiesEntity) = localDataSource.insertActivities(activitiesEntity)

    override  fun getLocalActivitiesAll() = localDataSource.getActivitiesAll()

    override  fun getLocalActivities(idPlot: String, pekerjaanId: String) = localDataSource.getActivities(idPlot, pekerjaanId)

    override  fun getLocalActivitiesByIdJob(idPlot: String, pekerjaanId: String) = localDataSource.getActivitiesByIdJob(idPlot, pekerjaanId)

    override suspend fun updateActivities(
        pekerja: String?,
        namaPekerjaan:String?,
        idPekerjaan: String?,
        nameActivity: String?,
        lat: String?,
        lng: String?,
        volume: String?,
        satuan: String?,
        photo: String?,
        workersId: String?,
        id: String?
    ) =
        localDataSource.updateActivities(
            pekerja,
            namaPekerjaan,
            idPekerjaan,
            nameActivity,
            lat,
            lng,
            volume,
            satuan,
            photo,
            workersId,
            id
        )

    override suspend fun insertAreaLocal(areaEntity: List<AreaEntity>) = localDataSource.insertArea(areaEntity)

    override  fun getLocalArea() = localDataSource.getArea()

    override suspend fun updateStatusArea(status: Boolean?, memberno: String?, statusDone: Boolean?) =
        localDataSource.updateArea(
            status,
            memberno,
            statusDone
        )

    override suspend fun deleteActivities() = localDataSource.deleteActivities()

    override suspend fun deleteArea() = localDataSource.deleteArea()

    override suspend fun deleteLocalItemActivities(id: Int?) = localDataSource.deleteItemActivities(id)

}
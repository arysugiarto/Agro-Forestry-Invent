package com.agro.inventory.data.repositories

import com.agro.inventory.data.source.callback.LocalSourceCallback
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.data.source.data.LocalDataSource


class LocalRepository(
    localDataSource: LocalDataSource
) : LocalSourceCallback {
    private val localDataSource = localDataSource

    override  fun getLocalActivitiesAll() = localDataSource.getActivitiesAll()

    override  fun getLocalActivities(idPlot: String, pekerjaanId: String) = localDataSource.getActivities(idPlot, pekerjaanId)

    override  fun getLocalActivitiesByIdJob(idPlot: String, pekerjaanId: String) = localDataSource.getActivitiesByIdJob(idPlot, pekerjaanId)


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

    override suspend fun insertInventLocal(inventEntity: InventEntity) = localDataSource.insertInvent(inventEntity)

    override fun getInvent(idComodity: String, kodePlot: String) = localDataSource.getInvent(idComodity, kodePlot)
    override suspend fun updateInvent(
        jmlTanam: String?,
        keliling: String?,
        tinggi: String?,
        idComodity: Int?,
        photo: String?,
        lat: String?,
        lng: String?,
        id: String?
    ) =
        localDataSource.updateInvent(
            jmlTanam,
            keliling,
            tinggi,
            idComodity,
            photo,
            lat,
            lng,
            id
        )


    override suspend fun insertReInventLocal(reInventEntity: ReinventEntity) = localDataSource.insertReInvent(reInventEntity)

    override fun getReInvent(idComodity: String, kodePlot: String) = localDataSource.getReInvent(idComodity,kodePlot)
    override suspend fun updateReInvent(
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
    ) =
        localDataSource.updateReInvent(
            jmlTanam,
            jmlHidup,
            jmlSakit,
            keliling,
            tinggi,
            photo,
            lat,
            lng,
            idComodity,
            id
        )

}
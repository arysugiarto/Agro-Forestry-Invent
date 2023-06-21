package com.agro.inventory.data.repositories

import com.agro.inventory.data.source.callback.LocalSourceCallback
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.data.source.data.LocalDataSource


class LocalRepository(
    localDataSource: LocalDataSource
) : LocalSourceCallback {
    private val localDataSource = localDataSource

    override suspend fun insertInventPlotLocal(inventPlotEntity: List<InventPlotEntity>) = localDataSource.insertInventPlot(inventPlotEntity)

    override  fun getLocalInventPlot() = localDataSource.getPlotInvent()

    override suspend fun updateStatusInventPlot(status: Boolean?, memberno: String?, statusDone: Boolean?) =
        localDataSource.updatePlotInvent(
            status,
            memberno,
            statusDone
        )

    override suspend fun deleteInventPlot() = localDataSource.deleteInventPlot()

    //input
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
package com.agro.inventory.data.repositories

import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.source.callback.LocalSourceCallback
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.data.source.data.LocalDataSource


class LocalRepository(
    localDataSource: LocalDataSource
) : LocalSourceCallback {
    private val localDataSource = localDataSource

    override suspend fun insertInventPlotLocal(inventPlotEntity: List<InventPlotEntity>) = localDataSource.insertInventPlot(inventPlotEntity)

    override  fun getLocalInventPlot(search: String) = localDataSource.getPlotInvent(search)

    override suspend fun updateStatusInventPlot(status:Boolean?, statusDone: Boolean?, kodePlot: String? ) =
        localDataSource.updatePlotInvent(
            status,
            statusDone,
            kodePlot

        )

     suspend fun deleteInventPlot() = localDataSource.deleteInventPlot()

    // reinvent

    override suspend fun insertReInventPlotLocal(reInventPlotEntity: List<ReInventPlotEntity>) = localDataSource.insertReInventPlot(reInventPlotEntity)

    override  fun getLocalReInventPlot(search: String) = localDataSource.getPlotReInvent(search)

    override suspend fun updateStatusReInventPlot(status: Boolean?,statusDone: Boolean?, kodePlot: String?) =
        localDataSource.updatePlotReInvent(
            status,
            statusDone,
            kodePlot
        )

    override suspend fun deleteReInventPlot() = localDataSource.deleteReInventPlot()


    //input
    override suspend fun insertInventLocal(inventEntity: InventEntity) = localDataSource.insertInvent(inventEntity)

    override fun getInvent(comodity: String, idComodity: String, kodePlot: String) = localDataSource.getInvent(comodity,idComodity, kodePlot)

    override fun getInventAll() = localDataSource.getInventAll()

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

    override fun getReInventAll() = localDataSource.getReInventAll()

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

    override suspend fun insertComodity(comodity: List<ComodityEntity>) = localDataSource.insertComodity(comodity)

    override fun getComodity(kodePlot: String) = localDataSource.getComodity(kodePlot)
    override suspend fun deleteComodity() = localDataSource.deleteComodity()

}
package com.agro.inventory.data.source.data

import com.agro.inventory.data.local.dao.InvenPlotDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.ReInvenPlotDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity


class LocalDataSource(

    inventPlotDao: InvenPlotDao,
    reInventPlotDao: ReInvenPlotDao,
    inventDao: InventDao,
    reInventDao: ReInventDao

) {
    private val daoInventPlot = inventPlotDao
    private val daoReInventPlot = reInventPlotDao
    private val daoInvent = inventDao
    private val daoReInvent = reInventDao


    suspend fun insertInventPlot(areaEntity: List<InventPlotEntity>) =
        daoInventPlot.insertPlotInvent(areaEntity)

    fun getPlotInvent(search: String) = daoInventPlot.getPlotInvent(search)

    suspend fun updatePlotInvent(
        status:Boolean?,
        statusDone: Boolean?,
        kodePlot: String?

    ) =
        daoInventPlot.updateStatusPlotInvent(
            status,
            statusDone,
            kodePlot

        )

    suspend fun deleteInventPlot() = daoInventPlot.deletePlotInvent()

    // reinvent
    suspend fun insertReInventPlot(reinventPlotEntity: List<ReInventPlotEntity>) =
        daoReInventPlot.insertPlotReInvent(reinventPlotEntity)

    fun getPlotReInvent(search: String) = daoReInventPlot.getPlotReInvent(search)

    suspend fun updatePlotReInvent(
        status: Boolean?,
        statusDone: Boolean?,
        kodePlot: String?
    ) =
        daoReInventPlot.updateStatusPlotReInvent(
            status,
            statusDone,
            kodePlot,

        )

    suspend fun deleteReInventPlot() = daoReInventPlot.deletePlotReInvent()


    //input

    suspend fun insertInvent(inventEntity: InventEntity) =
        daoInvent.insertInvent(inventEntity)

    fun getInvent(comodity:String, idComodity: String, kodePlot: String) =
        daoInvent.getInvent(comodity ,idComodity, kodePlot)

    fun getInventAll() =
        daoInvent.getInventAll()

    suspend fun updateInvent(
        jmlTanam: String?,
        keliling: String?,
        tinggi: String?,
        idComodity: Int?,
        photo: String?,
        lat: String?,
        lng: String?,
        id: String?
    ) =
        daoInvent.updateInvent(
            jmlTanam,
            keliling,
            tinggi,
            idComodity,
            photo,
            lat,
            lng,
            id
        )


    suspend fun insertReInvent(reInventEntity: ReinventEntity) =
        daoReInvent.insertReInvent(reInventEntity)

    fun getReInvent(idComodity: String, kodePlot: String) =
        daoReInvent.getReInvent(idComodity, kodePlot)
    fun getReInventAll() =
        daoReInvent.getReInventAll()

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
    ) =
        daoReInvent.updateReInvent(
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
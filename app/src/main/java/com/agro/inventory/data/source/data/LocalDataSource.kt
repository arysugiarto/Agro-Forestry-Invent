package com.agro.inventory.data.source.data

import com.agro.inventory.data.local.dao.AuthDao
import com.agro.inventory.data.local.dao.ComodityDao
import com.agro.inventory.data.local.dao.InvenPlotDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.InventDataDao
import com.agro.inventory.data.local.dao.ReInvenPlotDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.local.entity.AuthEntity
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity


class LocalDataSource(

    inventPlotDao: InvenPlotDao,
    reInventPlotDao: ReInvenPlotDao,
    inventDao: InventDao,
    reInventDao: ReInventDao,
    comodityDao: ComodityDao,
    inventDataDao: InventDataDao,
    authDao: AuthDao

) {
    private val daoInventPlot = inventPlotDao
    private val daoReInventPlot = reInventPlotDao
    private val daoInvent = inventDao
    private val daoReInvent = reInventDao
    private val daoComodity= comodityDao
    private val daoInventData= inventDataDao
    private val daoAuth = authDao


    suspend fun insertInventPlot(areaEntity: List<InventPlotEntity>) =
        daoInventPlot.insertPlotInvent(areaEntity)

    fun getPlotInvent(search: String) = daoInventPlot.getPlotInvent(search)

    fun getPlotInventByStatus(statusDone: Boolean?) = daoInventPlot.getPlotInventByStatus(statusDone)

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

    suspend fun deleteItemInventPlot(statusDone: Boolean?) = daoInventPlot.deleteItemInventPlotId(statusDone)

    suspend fun deleteItemInven(status: Boolean?) = daoInvent.deleteItemInvent(status)

    // reinvent
    suspend fun insertReInventPlot(reinventPlotEntity: List<ReInventPlotEntity>) =
        daoReInventPlot.insertPlotReInvent(reinventPlotEntity)

    fun getPlotReInvent(search: String) = daoReInventPlot.getPlotReInvent(search)

    fun getPlotReInventByStatus(statusDone: Boolean?) = daoReInventPlot.getPlotReInventByStatus(statusDone)

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

    suspend fun deleteItemReInventPlot(statusDone: Boolean?) = daoReInventPlot.deleteItemReInventPlotId(statusDone)

    suspend fun deleteItemReInven(status: Boolean?) = daoReInvent.deleteItemReInvent(status)

    //input

    suspend fun insertInvent(inventEntity: InventEntity) =
        daoInvent.insertInvent(inventEntity)

    fun getInvent(comodity:String, idComodity: String, kodePlot: String) =
        daoInvent.getInvent(comodity ,idComodity, kodePlot)

    fun getInventAll(status: Boolean?) =
        daoInvent.getInventAll(status)

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

    suspend fun updateStatusInvent(
        status:Boolean?,
        kodePlot: String?

    ) =
        daoInvent.updateStatusInvent(
            status,
            kodePlot

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
        jmlMati: String?,
        penyulaman: String?,
        keliling: String?,
        tinggi: String?,
        photo: String?,
        lat: String?,
        lng: String?,
        idComodity: Int?,
        jumlahReinvent: Int?,
        kodePlot: String?,
        comodity: String?,

    ) =
        daoReInvent.updateReInvent(
            jmlTanam,
            jmlHidup,
            jmlSakit,
            jmlMati,
            penyulaman,
            keliling,
            tinggi,
            photo,
            lat,
            lng,
            idComodity,
            jumlahReinvent,
            kodePlot,
            comodity,
        )

    suspend fun insertComodity(comodityEntity: List<ComodityEntity>) =
        daoComodity.insertComodity(comodityEntity)

    fun getComodity(kodePlot: String) =
        daoComodity.getComodity(kodePlot)

    suspend fun deleteComodity() = daoComodity.deleteComodity()


    suspend fun insertDataInvent(inventDataEntity: List<ReinventEntity>) =
        daoReInvent.insertInventData(inventDataEntity)

    fun getInventData(kodePlot: String) =
        daoReInvent.getInventData(kodePlot)

    suspend fun deleteInventData() = daoInventData.deleteInventData()


    suspend fun updateStatusComodity(
        status: Boolean?,
        kodePlot: String?,
        comodity: String?
    ) =
        daoComodity.updateStatusComodity(
            status,
            kodePlot,
            comodity
            )

    suspend fun insertAuth(authEntity: AuthEntity) =
        daoAuth.insertAuth(authEntity)

    fun getAuth() = daoAuth.getAuth()

    suspend fun deleteAuth() = daoAuth.deleteAuth()

    suspend fun updateStatusReInvent(
        status:Boolean?,
        kodePlot: String?

    ) =
        daoReInvent.updateStatusReInvent(
            status,
            kodePlot

        )



}
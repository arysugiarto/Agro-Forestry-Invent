package com.agro.inventory.data.source.data

import com.agro.inventory.data.local.dao.ActivitiesDao
import com.agro.inventory.data.local.dao.AreaDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity


class LocalDataSource(
    activitiesDao: ActivitiesDao,
    areaDao: AreaDao,
    inventDao: InventDao,
    reInventDao: ReInventDao

) {
    private val daoActivities = activitiesDao
    private val daoArea = areaDao
    private val daoInvent = inventDao
    private val daoReInvent = reInventDao



    fun getActivitiesAll() = daoActivities.getActivitiesAll()
    fun getActivities(idPlot: String, pekerjaanId: String) = daoActivities.getActivities(idPlot, pekerjaanId)

    fun getActivitiesByIdJob(idPlot: String, pekerjaanId: String) = daoActivities.getActivitiesByIdJob(idPlot, pekerjaanId)


    suspend fun updateActivities(pekerja: String?,
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
        daoActivities.updateActivities(
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

    suspend fun insertArea(areaEntity: List<AreaEntity>) =
        daoArea.insertArea(areaEntity)

    fun getArea() = daoArea.getArea()

    suspend fun updateArea(
        status: Boolean?,
        memberno:String?,
        statusDone: Boolean?
    ) =
        daoArea.updateStatusArea(
            status,
            memberno,
            statusDone,
        )

    suspend fun deleteActivities() = daoActivities.deleteActivities()

    suspend fun deleteArea() = daoArea.deleteArea()

    suspend fun deleteItemActivities(id: Int?) = daoActivities.deleteActivitiesId(id)


    suspend fun insertInvent(inventEntity: InventEntity) =
        daoInvent.insertInvent(inventEntity)

    suspend fun insertReInvent(reInventEntity: ReinventEntity) =
        daoReInvent.insertReInvent(reInventEntity)

}
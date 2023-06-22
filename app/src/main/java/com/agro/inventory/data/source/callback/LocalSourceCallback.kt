package com.agro.inventory.data.source.callback

import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import kotlinx.coroutines.flow.Flow

interface LocalSourceCallback {

    suspend fun insertInventPlotLocal(areaEntity: List<InventPlotEntity>)

    fun getLocalInventPlot(search: String): Flow<List<InventPlotEntity>>

    suspend fun updateStatusInventPlot(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?

    )
    suspend fun deleteInventPlot()

    //reinvent

    suspend fun insertReInventPlotLocal(areaEntity: List<ReInventPlotEntity>)

    fun getLocalReInventPlot(search: String): Flow<List<ReInventPlotEntity>>

    suspend fun updateStatusReInventPlot(
        status: Boolean?,
        memberno: String?,
        statusDone: Boolean?

    )
    suspend fun deleteReInventPlot()


//  input

    suspend fun insertReInventLocal(reinventEntity: ReinventEntity)

    fun getReInvent(idComodity: String, kodePlot: String): Flow<List<ReinventEntity>>

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

    fun getInvent(idComodity: String, kodePlot: String): Flow<List<InventEntity>>

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
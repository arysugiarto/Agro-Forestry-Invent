package com.agro.inventory.data.source.callback

import com.agro.inventory.data.local.entity.AuthEntity
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventDataEntity
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
        statusDone: Boolean?,
        kodePlot: String?

    )


    //reinvent

    suspend fun insertReInventPlotLocal(areaEntity: List<ReInventPlotEntity>)

    fun getLocalReInventPlot(search: String): Flow<List<ReInventPlotEntity>>

    suspend fun updateStatusReInventPlot(
        status: Boolean?,
        statusDone: Boolean?,
        kodePlot: String?
    )
    suspend fun deleteReInventPlot()


//  input

    suspend fun insertReInventLocal(reinventEntity: ReinventEntity)

    fun getReInvent(idComodity: String, kodePlot: String): Flow<List<ReinventEntity>>

    fun getReInventAll(): Flow<List<ReinventEntity>>

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
        jumlahreinvent: Int?,
        kodePlot: String?,
        comodity: String?,

    )

    suspend fun insertInventLocal(inventEntity: InventEntity)

    fun getInvent(comodity:String, idComodity: String, kodePlot: String): Flow<List<InventEntity>>

    fun getInventAll(): Flow<List<InventEntity>>

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

    suspend fun insertComodity(comodity: List<ComodityEntity>)
    fun getComodity(kodePlot: String): Flow<List<ComodityEntity>>

    suspend fun deleteComodity()

    suspend fun insertInventData(inventData: List<ReinventEntity>)
    fun getInventData(kodePlot: String): Flow<List<ReinventEntity>>

    suspend fun deleteInventData()

    suspend fun updateStatusComodity(
        status: Boolean?,
        kodePlot: String?,
        comodity: String?
    )

    suspend fun insertAuth(authEntity: AuthEntity)
    fun getAuth(): Flow<List<AuthEntity>>

    suspend fun deleteAuth()

}
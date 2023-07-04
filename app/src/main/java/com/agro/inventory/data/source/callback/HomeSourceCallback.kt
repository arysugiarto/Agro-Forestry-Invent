package com.agro.inventory.data.source.callback

import kotlinx.coroutines.flow.Flow
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.*
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.TaskPlotResponse
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import java.io.File


interface HomeSourceCallback {

    fun uploadImage(
        token: String?,
        sobiDate: String,
        gambar: File?
    ): Flow<Result<Files>>

    fun requestListPlot(
        token: String,
        sobiDate: String,
        areaID:String
    ): Flow<Result<ListPlotResponse>>

    fun requestDetailsPlot(
        token: String,
        sobiDate: String,
        plotId: String
    ): Flow<Result<DetailsPlotResponse>>

    fun requestSaveInventAll(token: String, sobiDate: String,bodyRequest: List<SaveInventBodyRequest.Data> ):
            Flow<Result<SaveInventBodyRequest.Data>>

    fun requestSaveReInventAll(token: String, sobiDate: String,bodyRequest: List<SaveReinventBodyRequest.Data> ):
            Flow<Result<SaveReinventBodyRequest.Data>>

    fun requestTaskPlot(
        token: String,
        sobiDate: String,
        userId:String
    ): Flow<Result<TaskPlotResponse>>


}
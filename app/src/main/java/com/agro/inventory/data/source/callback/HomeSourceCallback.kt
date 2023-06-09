package com.agro.inventory.data.source.callback

import kotlinx.coroutines.flow.Flow
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.*
import java.io.File


interface HomeSourceCallback {

    fun uploadImage(
        token: String?,
        sobiDate: String,
        gambar: File?
    ): Flow<Result<Files>>

    fun requestArea(
        token: String,
        sobiDate: String,
        userAccesId:String,
        memberId: String
    ): Flow<Result<AreaResponse>>

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

    fun requestSaveMonitoringWorkerAll(token: String, sobiDate: String,bodyRequest: List<AllMonitoringWorkerBodyRequest.Data> ):
            Flow<Result<AllMonitoringWorkerBodyRequest.Data>>


}
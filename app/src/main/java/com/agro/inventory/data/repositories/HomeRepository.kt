package com.agro.inventory.data.repositories

import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import com.agro.inventory.data.source.callback.HomeSourceCallback
import com.agro.inventory.data.source.data.HomeRemoteDataSource
import java.io.File

class HomeRepository(
    homeRemoteDataSource: HomeRemoteDataSource
) : HomeSourceCallback {
    private val remoteDataSource = homeRemoteDataSource

    override fun uploadImage(
        token: String?,
        sobiDate:String,
        gambar: File?
    ) = remoteDataSource.handleUploadImage(token, sobiDate, gambar)


    override fun requestListPlot(token:String, sobiDate:String, areaId:String) =
        remoteDataSource.requestListPlot(token,sobiDate, areaId)

    override fun requestDetailsPlot(token:String, sobiDate:String,plotid: String) =
        remoteDataSource.requestDetailsPlot(token,sobiDate,plotid)

    override fun requestSaveInventAll(
        token: String,
        role: String,
        bodyRequest: List<SaveInventBodyRequest.Data>
    ) =
        remoteDataSource.requestSaveInventAll(
            token,
            role,
            bodyRequest
        )

    override fun requestSaveReInventAll(
        token: String,
        role: String,
        bodyRequest: List<SaveReinventBodyRequest.Data>
    ) =
        remoteDataSource.requestSaveReInventAll(
            token,
            role,
            bodyRequest
        )


    override fun requestTaskPlot(token:String, sobiDate:String, userId:String) =
        remoteDataSource.requestTaskPlot(token,sobiDate, userId)



}
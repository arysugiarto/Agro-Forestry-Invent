package com.agro.inventory.data.remote.api

import com.google.gson.JsonElement
import com.agro.inventory.data.remote.model.*
import com.agro.inventory.data.remote.model.invent.ComodityResponse
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.TaskPlotResponse
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import com.agro.inventory.util.Const
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCallback {
    @POST(Const.NETWORK.LOGIN)
    suspend fun requestLogin(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Body jsonElement: JsonElement
    ): Response<LoginResponse>

    @Multipart
    @POST(Const.NETWORK.Media.UPLOAD)
    suspend fun requestEditImageTree(
        @Header("Authorization") token: String?,
        @Header("Sobi-Date") sobiDate: String,
        @Part gambar: MultipartBody.Part?
    ): Response<Files>

    @GET(Const.NETWORK.TOKEN)
    suspend fun requestToken(
    ): Response<TokenResponse>

    @GET(Const.NETWORK.LIST_PLOT)
    suspend fun requestListPlot(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("area_id") areaId: String,
    ): Response<ListPlotResponse>

    @GET(Const.NETWORK.PLOT_DETAILS )
    suspend fun requestDetailPlot(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("plot_id") plotId: String
    ): Response<DetailsPlotResponse>

    @POST(Const.NETWORK.UPLOAD_INVENT)
    suspend fun requestSaveInventAll(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Body body: JsonElement
    ): Response<SaveInventBodyRequest.Data>

    @POST(Const.NETWORK.UPLOAD_REINVENT)
    suspend fun requestSaveReInventAll(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Body body: JsonElement
    ): Response<SaveReinventBodyRequest.Data>

    @GET(Const.NETWORK.TASK_PLOT)
    suspend fun requestTaskPlot(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("userId") areaId: String,
    ): Response<TaskPlotResponse>

    @GET(Const.NETWORK.KOMODITAS)
    suspend fun requestKomoditas(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("userId") userId: String,
    ): Response<ComodityResponse>


}

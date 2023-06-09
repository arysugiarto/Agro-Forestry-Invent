package com.agro.inventory.data.remote.api

import com.google.gson.JsonElement
import com.agro.inventory.data.remote.model.*
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

    @GET(Const.NETWORK.AREA)
    suspend fun requestArea(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Query("user_access_id") userAccessId: String,
        @Query("member_id") memberId: String
    ): Response<AreaResponse>

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


    @POST(Const.NETWORK.SAVE_MONITORING_WORKER_ALL)
    suspend fun requestSaveMonitoringWorkerAll(
        @Header("Authorization") token: String,
        @Header("Sobi-Date") sobiDate: String,
        @Body body: JsonElement
    ): Response<AllMonitoringWorkerBodyRequest.Data>

}
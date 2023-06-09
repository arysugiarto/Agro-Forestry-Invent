package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class AllMonitoringWorkerBodyRequest(
    @SerializedName("data")
    val data: List<Data>? = null
) {
    data class Data(
        @SerializedName("monitoring")
        val monitoring: Monitoring? = null,
        @SerializedName("images")
        val images: String? = null,
    ) {
        data class Monitoring(
            @SerializedName("id_plot")
            val idPlot: Int?= null,
            @SerializedName("workers_id")
            val workersId: String?= null,
            @SerializedName("pekerjaan_id")
            val pekerjaanId: Int?= null,
            @SerializedName("activity_id")
            val activityId: Int?= null,
            @SerializedName("volume")
            val volume: String?= null,
            @SerializedName("photo")
            val photo: String?= null,
            @SerializedName("lat")
            val lat: String?= null,
            @SerializedName("lng")
            val lng: String?= null,
            @SerializedName("uid")
            val uid: String?= null,
            @SerializedName("app_source")
            val appSource: String?= null,
            @SerializedName("created_by")
            val createdBy: Int?= null,
            @SerializedName("deleted")
            val deleted: Int?= null
        )
    }
}
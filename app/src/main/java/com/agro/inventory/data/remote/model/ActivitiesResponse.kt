package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class ActivitiesResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("activity_name")
        val activityName: String,
        @SerializedName("satuan")
        val satuan : String
    )
}
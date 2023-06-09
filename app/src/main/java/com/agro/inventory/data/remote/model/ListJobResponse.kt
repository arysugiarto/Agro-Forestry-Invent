package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class ListJobResponse(
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
        @SerializedName("pekerjaan_name")
        val pekerjaanName: String
    )
}
package com.agro.inventory.data.remote.model.invent


import com.google.gson.annotations.SerializedName

data class ComodityResponse(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String?= null,
    @SerializedName("data")
    val `data`: List<Data>?= null
) {
    data class Data(
        @SerializedName("id_plot")
        val idPlot: Int?= null,
        @SerializedName("kode_plot")
        val kodePlot: String?= null,
        @SerializedName("id")
        val id: Int?= null,
        @SerializedName("komoditas")
        val komoditas: String?= null
    )
}
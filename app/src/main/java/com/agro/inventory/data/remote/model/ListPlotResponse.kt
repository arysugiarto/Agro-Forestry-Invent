package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class ListPlotResponse(
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
        @SerializedName("kode_plot")
        val kodePlot: String,
        @SerializedName("pola_tanam")
        val polaTanam: String,
        @SerializedName("komoditas")
        val komoditas: String,
        @SerializedName("polaTanamtxt")
        val polaTanamName: String,
    )
}
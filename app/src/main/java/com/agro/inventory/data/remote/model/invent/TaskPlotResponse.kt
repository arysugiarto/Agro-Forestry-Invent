package com.agro.inventory.data.remote.model.invent


import com.google.gson.annotations.SerializedName

data class TaskPlotResponse(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val `data`: List<Data>? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("kode_plot")
        val kodePlot: String? = null,
        @SerializedName("area_id")
        val areaId: Int? = null,
        @SerializedName("area_name")
        val areaName: String? = null,
        @SerializedName("member_name")
        val memberName: String? = null,
        @SerializedName("pola_tanam")
        val polaTanam: String? = null,
        @SerializedName("komoditas")
        val komoditas: String? = null,
        @SerializedName("komoditas_id")
        val komoditasId: Int? = null,
    )
}
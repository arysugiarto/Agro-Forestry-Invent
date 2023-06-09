package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class AreaResponse(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val `data`: List<Data>? = null
) {
    data class Data(
        @SerializedName("member_id")
        val memberId: Int? = null,
        @SerializedName("user_access_id")
        val userAccessId: Int? = null,
        @SerializedName("member_name")
        val memberName: String? = null,
        @SerializedName("member_no")
        val memberNo: String? = null,
        @SerializedName("area_id")
        val areaId: Int? = null,
        @SerializedName("nama_lahan")
        val namaLahan: String? = null,
        @SerializedName("jumlah_plot")
        val jumlahPlot: String? = null,
        @SerializedName("plot")
        val plot: List<Plot>? = null
    ) {
        data class Plot(
            @SerializedName("id")
            val id: Int? = null,
            @SerializedName("area_id")
            val areaId: Int? = null,
            @SerializedName("komoditas_id")
            val komoditasId: String? = null,
            @SerializedName("kode_plot")
            val kodePlot: String? = null,
            @SerializedName("created_on")
            val createdOn: String? = null,
            @SerializedName("pola_tanam")
            val polaTanam: Int? = null,
            @SerializedName("modified_on")
            val modifiedOn: Any? = null,
            @SerializedName("deleted")
            val deleted: Int? = null,
            @SerializedName("komoditas")
            val komoditas: String? = null,
            @SerializedName("member_id")
            val memberId: Int? = null,
            @SerializedName("member_name")
            val memberName: String? = null,
            @SerializedName("member_no")
            val memberNo: String? = null,
            @SerializedName("area_name")
            val areaName: String? = null,
            @SerializedName("monitoring_list")
            val monitoringList: List<Any>? = null
        )
    }
}
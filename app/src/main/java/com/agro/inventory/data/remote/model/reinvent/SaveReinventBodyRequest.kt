package com.agro.inventory.data.remote.model.reinvent


import com.google.gson.annotations.SerializedName

data class SaveReinventBodyRequest(
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("plants")
        val plants: Plants,
        @SerializedName("images")
        val images: String
    ) {
        data class Plants(
            @SerializedName("id")
            val id: Int,
            @SerializedName("plot_id")
            val plotId: Int,
            @SerializedName("plant_number")
            val plantNumber: Int,
            @SerializedName("total_plant")
            val totalPlant: Int,
            @SerializedName("alives_total")
            val alivesTotal: Int,
            @SerializedName("diseased_trees")
            val diseasedTrees: Int,
            @SerializedName("penyulaman_total")
            val penyulamanTotal: Int,
            @SerializedName("komoditas_id")
            val komoditasId: Int,
            @SerializedName("count_reinvent")
            val countReinvent: Int,
            @SerializedName("keliling")
            val keliling: String,
            @SerializedName("length")
            val length: Double,
            @SerializedName("user_id")
            val userId: Int,
            @SerializedName("lat")
            val lat: String,
            @SerializedName("lng")
            val lng: String,
            @SerializedName("uid")
            val uid: String,
            @SerializedName("app_source")
            val appSource: String,
            @SerializedName("created_by")
            val createdBy: Int,
            @SerializedName("deleted")
            val deleted: Int
        )
    }
}
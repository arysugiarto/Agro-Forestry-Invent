package com.agro.inventory.data.remote.model.invent


import com.google.gson.annotations.SerializedName

data class SaveInventBodyRequest(
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
            @SerializedName("plot_id")
            val plotId: Int,
            @SerializedName("plant_number")
            val plantNumber: Int,
            @SerializedName("total_plant")
            val totalPlant: Int,
            @SerializedName("komoditas_id")
            val komoditasId: Int,
            @SerializedName("keliling")
            val keliling: Double,
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
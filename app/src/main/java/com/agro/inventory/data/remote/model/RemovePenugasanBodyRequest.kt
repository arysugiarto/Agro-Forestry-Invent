package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class RemovePenugasanBodyRequest(
    @SerializedName("data")
    val `data`: List<Data>? = null
) {
    data class Data(
        @SerializedName("penugasan")
        val penugasan: Penugasan? = null
    ) {
        data class Penugasan(
            @SerializedName("id")
            val id: Int? = null
        )
    }
}
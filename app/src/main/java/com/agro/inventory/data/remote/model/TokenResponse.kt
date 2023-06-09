package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        @SerializedName("sobi-date")
        val sobiDate: Int,
        @SerializedName("authorization")
        val authorization: String
    )
}
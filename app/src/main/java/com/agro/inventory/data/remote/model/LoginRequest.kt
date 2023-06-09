package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("username")
        val username: String? = null,
        @SerializedName("password")
        val password: String? = null,
        @SerializedName("role_type")
        val roleType: Int? = null
    )
}
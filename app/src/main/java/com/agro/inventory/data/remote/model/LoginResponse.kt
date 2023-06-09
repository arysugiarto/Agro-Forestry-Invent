package com.agro.inventory.data.remote.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: List<Data>
) {
    data class Data(
        @SerializedName("user_access_id")
        val userAccessId: Int,
        @SerializedName("username")
        val username: String,
        @SerializedName("password_app")
        val passwordApp: String,
        @SerializedName("firstname")
        val firstname: String,
        @SerializedName("lastname")
        val lastname: String,
        @SerializedName("koperasi_id")
        val koperasiId: Int,
        @SerializedName("primary_device_id")
        val primaryDeviceId: String
    )
}
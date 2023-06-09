package com.agro.inventory.data.remote.model

import com.google.gson.annotations.SerializedName

data class Files(
    @SerializedName("nama_file")
    var namaFile: String? = "",
    @SerializedName("gambar")
    var files: List<File>? = listOf()
) {
    data class File(
        @SerializedName("original")
        var original: Original? = Original(),
        @SerializedName("url")
        var url: String? = "",
        @SerializedName("path")
        var path: String? = "",
        @SerializedName("filename")
        var filename: String? = "",
        @SerializedName("expired")
        var expired: String? = "",
        @SerializedName("extension")
        var extension: String? = "",
    ) {
        data class Original(
            @SerializedName("filename")
            var filename: String? = ""
        )
    }
}
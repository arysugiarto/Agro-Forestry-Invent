package com.agro.inventory.data.remote.model.reinvent


import com.google.gson.annotations.SerializedName

data class InventDataResponse(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    var `data`: List<Data>? = null,
) {
    data class Data(
        @SerializedName("komoditas")
        val komoditas: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("parent_id")
        val parentId: Any,
        @SerializedName("plot_id")
        val plotId: Int,
        @SerializedName("plant_number")
        val plantNumber: String,
        @SerializedName("dies_total")
        val diesTotal: Int,
        @SerializedName("alives_total")
        val alivesTotal: Int,
        @SerializedName("diseased_trees")
        val diseasedTrees: Int,
        @SerializedName("penyulaman_total")
        val penyulamanTotal: Int,
        @SerializedName("total_plant")
        val totalPlant: Int,
        @SerializedName("komoditas_id")
        val komoditasId: Int,
        @SerializedName("photo_aerial")
        val photoAerial: Any,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("diameter")
        val diameter: Double,
        @SerializedName("diameter_manual")
        val diameterManual: String,
        @SerializedName("keliling")
        val keliling: String,
        @SerializedName("keliling_manual")
        val kelilingManual: Any,
        @SerializedName("length")
        val length: Int,
        @SerializedName("length_manual")
        val lengthManual: Any,
        @SerializedName("length_200")
        val length200: Any,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("lng")
        val lng: String,
        @SerializedName("timestamp")
        val timestamp: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("valid_until")
        val validUntil: Any,
        @SerializedName("is_valid")
        val isValid: Int,
        @SerializedName("non_valid_reason")
        val nonValidReason: Any,
        @SerializedName("koperasi_id")
        val koperasiId: Any,
        @SerializedName("verification_date")
        val verificationDate: Any,
        @SerializedName("verification_by")
        val verificationBy: Any,
        @SerializedName("verification_status")
        val verificationStatus: Int,
        @SerializedName("uid")
        val uid: String,
        @SerializedName("app_source")
        val appSource: String,
        @SerializedName("last_adjustment")
        val lastAdjustment: Any,
        @SerializedName("adjustment_sequence")
        val adjustmentSequence: Any,
        @SerializedName("booking_contract_id")
        val bookingContractId: Any,
        @SerializedName("created_on")
        val createdOn: String,
        @SerializedName("created_by")
        val createdBy: Int,
        @SerializedName("modified_on")
        val modifiedOn: Any,
        @SerializedName("modified_by")
        val modifiedBy: Any,
        @SerializedName("deleted")
        val deleted: Int,
        @SerializedName("available_status")
        val availableStatus: Int,
        @SerializedName("available_updated_at")
        val availableUpdatedAt: Any,
        @SerializedName("status_pup")
        val statusPup: Any,
        @SerializedName("status_inside_delineasi")
        val statusInsideDelineasi: Any,
        @SerializedName("akurasi_maps")
        val akurasiMaps: Any,
        @SerializedName("notes")
        val notes: Any,
        @SerializedName("count_reinvent")
        val countReinvent: Any
    )
}
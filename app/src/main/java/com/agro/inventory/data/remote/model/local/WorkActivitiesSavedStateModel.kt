package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class WorkActivitiesSavedStateModel(
    val id : String? = null,
    val name : String? = null,
    val satuan : String? = null,
    var state: Boolean? = null
) : Parcelable
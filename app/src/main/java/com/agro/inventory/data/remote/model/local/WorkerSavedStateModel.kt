package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class WorkerSavedStateModel(
    val id : String? = null,
    val name : String? = null,
    var state: Boolean? = null
) : Parcelable
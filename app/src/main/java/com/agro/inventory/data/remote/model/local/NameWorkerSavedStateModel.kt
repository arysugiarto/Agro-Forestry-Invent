package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class NameWorkerSavedStateModel(
    var data: List<AddNameWorker> = emptyList(),
    var state: Boolean? = null,
    var checkedAll: Boolean = false
) : Parcelable
package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class CodePlotSavedStateModel(
    val code : String? = null,
    val croppingPattern: String? = null,
    val comodity: String? = null,
    var state: Boolean? = null
) : Parcelable
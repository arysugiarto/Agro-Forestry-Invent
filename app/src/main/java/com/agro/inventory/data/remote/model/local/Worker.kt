package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class Worker(
    var name: String? = null,
): Parcelable
package com.agro.inventory.data.remote.model.local

import android.os.Parcelable
import com.agro.inventory.util.emptyString
import kotlinx.parcelize.Parcelize



@Parcelize
data class AddNameWorker(
    var id: String = emptyString,
    var name: String = emptyString,
    var idWorkers: String = emptyString
): Parcelable
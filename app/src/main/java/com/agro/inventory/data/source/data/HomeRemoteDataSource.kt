package com.agro.inventory.data.source.data

import com.agro.inventory.data.remote.api.ApiCallback
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import com.agro.inventory.util.flowResponse
import com.agro.inventory.util.gson
import com.agro.inventory.util.partFile
import com.agro.inventory.util.toJsonElement
import java.io.File

class HomeRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback


    fun handleUploadImage(
        token: String?,
        sobiDate:String,
        gambar: File?
    ) = flowResponse {
        val part = gambar.partFile() ?: throw IllegalAccessException("Image Part must be not Null")

        apiCallback.requestEditImageTree(
            token,
            sobiDate,
            part
        )
    }

    fun requestListPlot(token: String, sobiDate: String, areaid: String) =
        flowResponse {
            apiCallback.requestListPlot(token, sobiDate, areaid)
        }

    fun requestDetailsPlot(token: String, sobiDate: String, plotId: String) =
        flowResponse {
            apiCallback.requestDetailPlot(token, sobiDate, plotId)
        }


    fun requestSaveInventAll(token: String, sobiDate: String,saveInventBodyRrequest: List<SaveInventBodyRequest.Data>) = flowResponse {
        val data = gson.toJsonTree(saveInventBodyRrequest)

        val body = gson.toJsonElement {
            put("data", data)
        }
        apiCallback.requestSaveInventAll(token, sobiDate, body)

    }

    fun requestSaveReInventAll(token: String, sobiDate: String,saveReInventBodyRrequest: List<SaveReinventBodyRequest.Data>) = flowResponse {
        val data = gson.toJsonTree(saveReInventBodyRrequest)

        val body = gson.toJsonElement {
            put("data", data)
        }
        apiCallback.requestSaveReInventAll(token, sobiDate, body)

    }

    fun requestTaskPlot(token: String, sobiDate: String, userId: String) =
        flowResponse {
            apiCallback.requestTaskPlot(token, sobiDate, userId)
        }

}
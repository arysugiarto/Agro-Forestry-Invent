package com.agro.inventory.data.source.data

import com.agro.inventory.data.remote.api.ApiCallback
import com.agro.inventory.data.remote.model.AllMonitoringWorkerBodyRequest
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

    fun requestArea(token: String, sobiDate: String, userAccesId: String, memberId: String) =
        flowResponse {
            apiCallback.requestArea(token, sobiDate, userAccesId, memberId)
        }

    fun requestListPlot(token: String, sobiDate: String, areaid: String) =
        flowResponse {
            apiCallback.requestListPlot(token, sobiDate, areaid)
        }

    fun requestDetailsPlot(token: String, sobiDate: String, plotId: String) =
        flowResponse {
            apiCallback.requestDetailPlot(token, sobiDate, plotId)
        }

    fun requestListJob(token: String, sobiDate: String) =
        flowResponse {
            apiCallback.requestListJob(token, sobiDate)
        }

    fun requestActivities(token: String, sobiDate: String, jobId: String) =
        flowResponse {
            apiCallback.requestActivities(token, sobiDate, jobId)
        }

    fun requestWorker(token: String, sobiDate: String, mandorId: String) =
        flowResponse {
            apiCallback.requestWorker(token, sobiDate, mandorId)
        }

    fun requestSaveMonitoringWorkerAll(token: String, sobiDate: String,allMonitoringWorkerBodyRequest: List<AllMonitoringWorkerBodyRequest.Data>) = flowResponse {
            val data = gson.toJsonTree(allMonitoringWorkerBodyRequest)

            val body = gson.toJsonElement {
                put("data", data)
            }
            apiCallback.requestSaveMonitoringWorkerAll(token, sobiDate, body)

    }

}
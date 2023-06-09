package com.agro.inventory.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull

object Const {

    object NETWORK {
        const val PREFIX_GENERAL = "v1/general/"
        const val SESSION_ID = "session_id"
        const val EQUALS = "="
        const val PREFIX = "v2"

        const val LOGIN = "$PREFIX/user/login"
        const val TOKEN = "token/generator"
        const val AREA = "$PREFIX/plot/getAllData"
        const val LIST_PLOT = "$PREFIX/plot/listPlot"
        const val PLOT_DETAILS = "$PREFIX/plot/getPlot"
        const val LIST_JOB = "$PREFIX/activity/listPekerjaan"
        const val ACTIVITIES = "$PREFIX/activity/listActivity"
        const val WORKER = "$PREFIX/activity/listWorkers"
        const val SAVE_MONITORING_WORKER_ALL = "$PREFIX/monitoring/upload_datav2"

        object Media {
            const val UPLOAD = "$PREFIX/monitoring/upload_photo"
            const val FILE_FIELD = "file[]"
        }

    }

    object MediaType {
        val image = "image/jpeg".toMediaTypeOrNull()
        val text = "text/plain".toMediaTypeOrNull()
    }

    object Database {
        const val DATABASE_NAME = "AGRO_DATABASE"

        object Table {
            const val ACTIVITIES = "activities_entity"
            const val AREA = "area_entity"
            const val ACTIVITIES_AREA = "activities_area_entity"
        }
    }

    object Access {
        const val AUTH_PREFIX = "Bearer"
        const val AUTH = "Bearer"
    }

    object File {
        object Location {
            const val basePath = "agroforestry/"
            const val storePath = "Store/"
        }

        object MimeType {
            const val image = "image/jpeg"
            const val pdf = "application/pdf"
        }

        object Image {
            const val defaultFileName = "Agro-Image"
        }

        object Pending {
            const val isPending = 1
            const val notPending = 0
        }

    }


    object Paging {
        const val PER_PAGE_SMALL = 10
        const val PER_PAGE_MEDIUM = 25
        const val PER_PAGE_LARGE = 50
        const val BEGIN_PAGE = 1
        const val NOTIFICATION_PAGE = 4
    }

    object PRODUCT {
        const val NOT_AVAILABLE = "N/A"
    }

    object SweetAlertType {
        const val MESSAGE = "message"
        const val ERROR = "error"
        const val SUCCESS = "success"
        const val WARNING = "warning"
    }

    object General {
        const val path = "path"
    }

}
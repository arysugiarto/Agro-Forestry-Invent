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
        const val LIST_PLOT = "$PREFIX/plot/listPlot"
        const val PLOT_DETAILS = "$PREFIX/plot/getPlot"
        const val UPLOAD_INVENT = "$PREFIX/plant/upload_data"
        const val UPLOAD_REINVENT = "$PREFIX/plant/upload_reinventdata"
        const val TASK_PLOT = "$PREFIX/plot/getPlotByUser"

        const val KOMODITAS ="$PREFIX/plot/getKomoditasUserId"

        const val INVENT_DATA = "$PREFIX/plot/getDataInven"

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
            const val PLOT_INVENT = "plot_invent_entity"
            const val PLOT_REINVENT = "plot_reinvent_entity"
            const val INVENT = "invent_entity"
            const val REINVENT = "reinvent_entity"
            const val COMODITY = "comodity_entity"
            const val INVENT_DATA = "invent_data_entity"
            const val AUTH = "auth_entity"
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
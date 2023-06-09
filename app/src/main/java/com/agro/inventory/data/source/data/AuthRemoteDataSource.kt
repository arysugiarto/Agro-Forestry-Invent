package com.agro.inventory.data.source.data

import com.agro.inventory.data.remote.api.ApiCallback
import com.agro.inventory.data.remote.model.LoginRequest
import com.agro.inventory.util.flowResponse
import com.agro.inventory.util.gson

class AuthRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestLogin(token:String, sobiDate: String, loginRequest: LoginRequest) =
        flowResponse {
            val body = gson.toJsonTree(loginRequest)

            apiCallback.requestLogin(token,sobiDate, body)
        }

    fun requestToken() =
        flowResponse {
            apiCallback.requestToken()
        }

}
package com.agro.inventory.data.repositories

import com.agro.inventory.data.remote.model.LoginRequest
import com.agro.inventory.data.source.callback.AuthSourceCallback
import com.agro.inventory.data.source.data.AuthRemoteDataSource

class AuthRepository(
    authRemoteDataSource: AuthRemoteDataSource
) : AuthSourceCallback {
    private val remoteDataSource = authRemoteDataSource

    override fun requestLogin(token:String, sobiDate:String, loginRequest: LoginRequest) =
        remoteDataSource.requestLogin(token, sobiDate, loginRequest)

    override fun requestToken() =
        remoteDataSource.requestToken()


}
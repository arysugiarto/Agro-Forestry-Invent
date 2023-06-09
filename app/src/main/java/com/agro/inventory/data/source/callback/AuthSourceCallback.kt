package com.agro.inventory.data.source.callback

import kotlinx.coroutines.flow.Flow
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.LoginRequest
import com.agro.inventory.data.remote.model.LoginResponse
import com.agro.inventory.data.remote.model.TokenResponse


interface AuthSourceCallback {
    fun requestLogin(
        token:String,
        sobiDate:String,
        loginRequest: LoginRequest
    ): Flow<Result<LoginResponse>>

    fun requestToken(
    ): Flow<Result<TokenResponse>>


}
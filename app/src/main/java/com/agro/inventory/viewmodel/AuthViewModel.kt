package com.agro.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.agro.inventory.data.remote.model.LoginResponse
import com.agro.inventory.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.agro.inventory.data.repositories.AuthRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.LoginRequest
import com.agro.inventory.data.remote.model.TokenResponse
import com.agro.inventory.util.livevent.Event

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    authRepository: AuthRepository
) : BaseViewModel(application) {
    private val repository = authRepository

    private var _login: MutableLiveData<com.agro.inventory.data.remote.Result<LoginResponse>> = MutableLiveData()
    val login: LiveData<Result<LoginResponse>> get() = _login
    private var _token: MutableLiveData<Event<Result<TokenResponse>>> = MutableLiveData()
    val token: LiveData<Event<Result<TokenResponse>>>get() = _token

    private var _username: MutableLiveData<String> = MutableLiveData()
    val username: LiveData<String> get() = _username

    private var _password: MutableLiveData<String> = MutableLiveData()
    val password: LiveData<String> get() = _password

    private var _tokenDatastore: MutableLiveData<String> = MutableLiveData()
    val tokenDatastore: LiveData<String> get() = _tokenDatastore
    private var _sobiDateDatastore: MutableLiveData<String> = MutableLiveData()
    val sobiDateDatastore: LiveData<String> get() = _sobiDateDatastore

    private var _session: MutableLiveData<Boolean> = MutableLiveData()
    val session: LiveData<Boolean> get() = _session

    private var _useraccess: MutableLiveData<String> = MutableLiveData()
    val useraccess: LiveData<String> get() = _useraccess




    fun getUsernameDataStore() = accessManager.accessUsername.onEach {
        _username.value = it
    }.launchIn(viewModelScope)

    fun getPasswordDataStore() = accessManager.accessPassword.onEach {
        _password.value = it
    }.launchIn(viewModelScope)

    fun getTokenDataStore() = accessManager.accessToken.onEach {
        _tokenDatastore.value = it
    }.launchIn(viewModelScope)

    fun getSobiDataStore() = accessManager.accessSobiDate.onEach {
        _sobiDateDatastore.value = it
    }.launchIn(viewModelScope)
    fun getSession() = accessManager.accesSession.onEach {
        _session.value = it
    }.launchIn(viewModelScope)

    fun getUserAccessId() = accessManager.accessId.onEach {
        _useraccess.value = it
    }.launchIn(viewModelScope)


    fun requestToken() =
        repository.requestToken()
            .onEach { result ->
                _token.value = Event(result)

                if (result is Result.Success) {
                    val tokenDataStore = result.data?.data?.authorization.toString()
                    val sobiDate = result.data?.data?.sobiDate.toString()
                    _tokenDatastore.value = tokenDataStore
                    _sobiDateDatastore.value = sobiDate

                }
            }.launchIn(viewModelScope)


    fun requestLogin(
        token:String,
        sobiDate:String,
        loginRequest: LoginRequest
    ) = repository.requestLogin(token,sobiDate, loginRequest)
        .onEach { result ->
            _login.value = result

            if (result is Result.Success) {
                val username = result.data?.data?.firstOrNull()?.username.toString()
                val password = result.data?.data?.firstOrNull()?.passwordApp.toString()

                _username.value = username
                _password.value = password
//                accessManager.setSession(true)

            }
        }.launchIn(viewModelScope)


}
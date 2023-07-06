package com.agro.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.agro.inventory.base.BaseViewModel
import com.agro.inventory.data.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.agro.inventory.util.livevent.Event
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.*
import com.agro.inventory.data.remote.model.invent.ComodityResponse
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.TaskPlotResponse
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {
    private val repository = homeRepository
    private val stateHandler = savedStateHandle

    private var _plot: MutableLiveData<Event<Result<ListPlotResponse>>> = MutableLiveData()
    val plot: LiveData<Event<Result<ListPlotResponse>>>get() = _plot

    private var _taskPlot: MutableLiveData<Event<Result<TaskPlotResponse>>> = MutableLiveData()
    val taskPlot: LiveData<Event<Result<TaskPlotResponse>>>get() = _taskPlot

    private var _comodity: MutableLiveData<Event<Result<ComodityResponse>>> = MutableLiveData()
    val comodity: LiveData<Event<Result<ComodityResponse>>>get() = _comodity


    private var _saveInventAll: MutableLiveData<Event<Result<SaveInventBodyRequest.Data>>> = MutableLiveData()
    val saveInventAll: LiveData<Event<Result<SaveInventBodyRequest.Data>>> get() = _saveInventAll

    private var _saveReInventAll: MutableLiveData<Event<Result<SaveReinventBodyRequest.Data>>> = MutableLiveData()
    val saveReInventAll: LiveData<Event<Result<SaveReinventBodyRequest.Data>>> get() = _saveReInventAll


    fun requestTaskPlot(token: String,sobiDate:String, userId:String) =
        repository.requestTaskPlot(token,sobiDate, userId)
            .onEach { result ->
                _taskPlot.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestComodity(token: String,sobiDate:String, userId:String) =
        repository.requestComodity(token,sobiDate, userId)
            .onEach { result ->
                _comodity.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestListPlot(token: String,sobiDate:String, areaId:String) =
        repository.requestListPlot(token,sobiDate, areaId)
            .onEach { result ->
                _plot.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestSaveInventAll(token: String, sobiDate: String, bodyRequest: List<SaveInventBodyRequest.Data>) =
        repository.requestSaveInventAll (token, sobiDate, bodyRequest).onEach { result ->
            _saveInventAll.value = Event(result)
        }.launchIn(viewModelScope)

    fun requestSaveReInventAll(token: String, sobiDate: String, bodyRequest: List<SaveReinventBodyRequest.Data>) =
        repository.requestSaveReInventAll (token, sobiDate, bodyRequest).onEach { result ->
            _saveReInventAll.value = Event(result)
        }.launchIn(viewModelScope)


    fun setSaveAllInventNothing() {
        _saveInventAll.value = Event(Result.nothing())
    }

    fun setSaveAllReInventNothing() {
        _saveReInventAll.value = Event(Result.nothing())
    }


}


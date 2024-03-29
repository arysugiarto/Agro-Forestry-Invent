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
import com.agro.inventory.data.remote.model.invent.TaskPlotReinventResponse
import com.agro.inventory.data.remote.model.invent.TaskPlotResponse
import com.agro.inventory.data.remote.model.reinvent.InventDataResponse
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

    private var _taskPlotReinvent: MutableLiveData<Event<Result<TaskPlotReinventResponse>>> = MutableLiveData()
    val taskPlotReinvent: LiveData<Event<Result<TaskPlotReinventResponse>>>get() = _taskPlotReinvent


    private var _comodity: MutableLiveData<Event<Result<ComodityResponse>>> = MutableLiveData()
    val comodity: LiveData<Event<Result<ComodityResponse>>>get() = _comodity

    private var _inventData: MutableLiveData<Event<Result<InventDataResponse>>> = MutableLiveData()
    val inventData: LiveData<Event<Result<InventDataResponse>>>get() = _inventData



    private var _saveInventAll: MutableLiveData<Event<Result<SaveInventBodyRequest.Data>>> = MutableLiveData()
    val saveInventAll: LiveData<Event<Result<SaveInventBodyRequest.Data>>> get() = _saveInventAll

    private var _saveReInventAll: MutableLiveData<Event<Result<SaveReinventBodyRequest.Data>>> = MutableLiveData()
    val saveReInventAll: LiveData<Event<Result<SaveReinventBodyRequest.Data>>> get() = _saveReInventAll


    private var _removeAssigment: MutableLiveData<Event<Result<RemovePenugasanBodyRequest.Data>>> = MutableLiveData()
    val removeAssigment: LiveData<Event<Result<RemovePenugasanBodyRequest.Data>>> get() = _removeAssigment



    fun requestTaskPlot(token: String,sobiDate:String, userId:String) =
        repository.requestTaskPlot(token,sobiDate, userId)
            .onEach { result ->
                _taskPlot.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestTaskPlotReinvent(token: String,sobiDate:String, userId:String) =
        repository.requestTaskPlotReinvent(token,sobiDate, userId)
            .onEach { result ->
                _taskPlotReinvent.value = Event(result)
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

    fun requestInventData(token: String,sobiDate:String, userId:String) =
        repository.requestInventData(token,sobiDate, userId)
            .onEach { result ->
                _inventData.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestRemoveAssigment(token: String, sobiDate: String, bodyRequest: List<RemovePenugasanBodyRequest.Data>) =
        repository.requestRemoveAssigment (token, sobiDate, bodyRequest).onEach { result ->
            _removeAssigment.value = Event(result)
        }.launchIn(viewModelScope)



    fun setSaveAllInventNothing() {
        _saveInventAll.value = Event(Result.nothing())
    }

    fun setSaveAllReInventNothing() {
        _saveReInventAll.value = Event(Result.nothing())
    }

    fun setRemoveAssigmentNothing() {
        _removeAssigment.value = Event(Result.nothing())
    }


}


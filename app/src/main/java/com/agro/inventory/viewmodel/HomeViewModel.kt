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
import java.io.File


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {
    private val repository = homeRepository
    private val stateHandler = savedStateHandle

    private var _area: MutableLiveData<Result<AreaResponse>> = MutableLiveData()
    val area: LiveData<Result<AreaResponse>>get() = _area
    private var _plot: MutableLiveData<Event<Result<ListPlotResponse>>> = MutableLiveData()
    val plot: LiveData<Event<Result<ListPlotResponse>>>get() = _plot
    private var _plotDetails: MutableLiveData<Event<Result<DetailsPlotResponse>>> = MutableLiveData()
    val plotDetails: LiveData<Event<Result<DetailsPlotResponse>>>get() = _plotDetails


    private var _saveMonitoringWorkerAll: MutableLiveData<Event<Result<AllMonitoringWorkerBodyRequest.Data>>> = MutableLiveData()
    val saveMonitoringWorkerAll: LiveData<Event<Result<AllMonitoringWorkerBodyRequest.Data>>> get() = _saveMonitoringWorkerAll


    private val _uploadResult: MutableLiveData<Result<Files>> = MutableLiveData()
    val uploadResult: LiveData<Result<Files>> get() = _uploadResult

    private var _areaDatastore: MutableLiveData<String> = MutableLiveData()
    val areaDatastore: LiveData<String> get() = _areaDatastore
    private var _nameMemberDatastore: MutableLiveData<String> = MutableLiveData()
    val nameMemberDatastore: LiveData<String> get() = _nameMemberDatastore
    private var _noMemberDatastore: MutableLiveData<String> = MutableLiveData()
    val noMemberDatastore: LiveData<String> get() = _noMemberDatastore
    private var _plotDatastore: MutableLiveData<String> = MutableLiveData()
    val plotDatastore: LiveData<String> get() = _plotDatastore


    fun getAreaDataStore() = accessManager.accessArea.onEach {
        _areaDatastore.value = it.toString()
    }.launchIn(viewModelScope)

    fun getNameMemberDataStore() = accessManager.accessNameMember.onEach {
        _nameMemberDatastore.value = it.toString()
    }.launchIn(viewModelScope)

    fun getNoMemberDataStore() = accessManager.accessNoMember.onEach {
        _noMemberDatastore.value = it.toString()
    }.launchIn(viewModelScope)

    fun getPlotDataStore() = accessManager.accessPlot.onEach {
        _plotDatastore.value = it.toString()
    }.launchIn(viewModelScope)


    fun requestArea(token: String,sobiDate:String,userAccessId:String, memberId:String) =
        repository.requestArea(token,sobiDate,userAccessId,memberId)
            .onEach { result ->
                _area.value = result
            }.launchIn(viewModelScope)

    fun requestListPlot(token: String,sobiDate:String, areaId:String) =
        repository.requestListPlot(token,sobiDate, areaId)
            .onEach { result ->
                _plot.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestDetailsPlot(token: String,sobiDate:String,plotId:String) =
        repository.requestDetailsPlot(token,sobiDate, plotId)
            .onEach { result ->
                _plotDetails.value = Event(result)
            }.launchIn(viewModelScope)


    fun requestSaveMonitoringWorkerAll(token: String, sobiDate: String, bodyRequest: List<AllMonitoringWorkerBodyRequest.Data>) =
        repository.requestSaveMonitoringWorkerAll (token, sobiDate, bodyRequest).onEach { result ->
            _saveMonitoringWorkerAll.value = Event(result)
        }.launchIn(viewModelScope)


    fun requestImageUploadResult(token: String, sobiDate: String, gambar: File?) =
        repository.uploadImage(token, sobiDate, gambar)
            .onEach { result -> _uploadResult.value = result
    }.launchIn(viewModelScope)


    fun setSaveAllMonitoringNothing() {
        _saveMonitoringWorkerAll.value = Event(Result.nothing())
    }

companion object{
    const val CODE_PLOT = "code_plot_state_key"
    const val NAME_WORKER = "name_worker_state_key"
    const val WORK_ACTIVITIES = "work_activities_state_key"
}
}
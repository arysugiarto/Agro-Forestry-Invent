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
import com.agro.inventory.data.remote.model.local.*
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
    private var _listJob: MutableLiveData<Event<Result<ListJobResponse>>> = MutableLiveData()
    val listJob: LiveData<Event<Result<ListJobResponse>>>get() = _listJob
    private var _activities: MutableLiveData<Event<Result<ActivitiesResponse>>> = MutableLiveData()
    val activities: LiveData<Event<Result<ActivitiesResponse>>>get() = _activities
    private var _worker: MutableLiveData<Event<Result<WorkerResponse>>> = MutableLiveData()
    val worker: LiveData<Event<Result<WorkerResponse>>>get() = _worker


    private var _saveMonitoringWorkerAll: MutableLiveData<Event<Result<AllMonitoringWorkerBodyRequest.Data>>> = MutableLiveData()
    val saveMonitoringWorkerAll: LiveData<Event<Result<AllMonitoringWorkerBodyRequest.Data>>> get() = _saveMonitoringWorkerAll


    private var _setCodePlot: MutableLiveData<Event<CodePlotSavedStateModel>> = MutableLiveData()
    val setCodePlot: LiveData<Event<CodePlotSavedStateModel>> get() = _setCodePlot

    private var _setWorker: MutableLiveData<Event<WorkerSavedStateModel>> = MutableLiveData()
    val setWorker: LiveData<Event<WorkerSavedStateModel>> get() = _setWorker

    private var _setNameWorker: MutableLiveData<Event<NameWorkerSavedStateModel>> = MutableLiveData()
    val setNameWorker: LiveData<Event<NameWorkerSavedStateModel>> get() = _setNameWorker

    private var _setAddNameWorker: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val setAddNameWorker: LiveData<Event<Boolean>> get() = _setAddNameWorker


    private var _setAddActivites: MutableLiveData<Event<WorkActivitiesSavedStateModel>> = MutableLiveData()
    val setAddActivites: LiveData<Event<WorkActivitiesSavedStateModel>> get() = _setAddActivites

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


    var codePlotSavedState: CodePlotSavedStateModel
        get() = stateHandler[CODE_PLOT] ?: CodePlotSavedStateModel()
        set(value) = stateHandler.set(CODE_PLOT, value)

    var nameWorkerSavedState: NameWorkerSavedStateModel
        get() = stateHandler[NAME_WORKER] ?: NameWorkerSavedStateModel()
        set(value) = stateHandler.set(NAME_WORKER, value)

    var workActivitiesSavedState: WorkActivitiesSavedStateModel
        get() = stateHandler[WORK_ACTIVITIES] ?: WorkActivitiesSavedStateModel()
        set(value) = stateHandler.set(WORK_ACTIVITIES, value)


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

    fun requestListJob(token: String,sobiDate:String) =
        repository.requestListJob(token,sobiDate)
            .onEach { result ->
                _listJob.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestActivities(token: String,sobiDate:String,jobId:String) =
        repository.requestActivities(token,sobiDate, jobId)
            .onEach { result ->
                _activities.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestWorker(token: String,sobiDate:String,mandorId:String) =
        repository.requestWorker(token,sobiDate, mandorId)
            .onEach { result ->
                _worker.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestSaveMonitoringWorkerAll(token: String, sobiDate: String, bodyRequest: List<AllMonitoringWorkerBodyRequest.Data>) =
        repository.requestSaveMonitoringWorkerAll (token, sobiDate, bodyRequest).onEach { result ->
            _saveMonitoringWorkerAll.value = Event(result)
        }.launchIn(viewModelScope)


    fun requestImageUploadResult(token: String, sobiDate: String, gambar: File?) =
        repository.uploadImage(token, sobiDate, gambar)
            .onEach { result -> _uploadResult.value = result
    }.launchIn(viewModelScope)


    fun setCodePlot(code: CodePlotSavedStateModel) {
        _setCodePlot.value = Event(code)
    }

    fun setWorker(worker: WorkerSavedStateModel) {
        _setWorker.value = Event(worker)
    }
    fun setNameWorker() {
        _setAddNameWorker.value = Event(true)
    }

    fun setActivities(worker: WorkActivitiesSavedStateModel) {
        _setAddActivites.value = Event(worker)
    }


    fun setCodePlotNothing() {
        _setCodePlot.value = Event(CodePlotSavedStateModel())
    }

    fun setWorkerNothing() {
        _setWorker.value = Event(WorkerSavedStateModel())
    }
    fun setNameWorkerNothing() {
        _setAddNameWorker.value = Event(false)
    }

    fun setAddActivitesNothing() {
        _setAddActivites.value = Event(WorkActivitiesSavedStateModel())
    }

    fun setSaveAllMonitoringNothing() {
        _saveMonitoringWorkerAll.value = Event(Result.nothing())
    }

companion object{
    const val CODE_PLOT = "code_plot_state_key"
    const val NAME_WORKER = "name_worker_state_key"
    const val WORK_ACTIVITIES = "work_activities_state_key"
}
}
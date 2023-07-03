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

    private var _plot: MutableLiveData<Event<Result<ListPlotResponse>>> = MutableLiveData()
    val plot: LiveData<Event<Result<ListPlotResponse>>>get() = _plot

    private var _saveInventAll: MutableLiveData<Event<Result<SaveInventBodyRequest.Data>>> = MutableLiveData()
    val saveInventAll: LiveData<Event<Result<SaveInventBodyRequest.Data>>> get() = _saveInventAll



    fun requestListPlot(token: String,sobiDate:String, areaId:String) =
        repository.requestListPlot(token,sobiDate, areaId)
            .onEach { result ->
                _plot.value = Event(result)
            }.launchIn(viewModelScope)

    fun requestSaveInventAll(token: String, sobiDate: String, bodyRequest: List<SaveInventBodyRequest.Data>) =
        repository.requestSaveInventAll (token, sobiDate, bodyRequest).onEach { result ->
            _saveInventAll.value = Event(result)
        }.launchIn(viewModelScope)

    fun setSaveAllInventNothing() {
        _saveInventAll.value = Event(Result.nothing())
    }


}


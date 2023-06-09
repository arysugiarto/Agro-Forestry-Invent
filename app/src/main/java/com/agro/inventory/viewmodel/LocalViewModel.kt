package com.agro.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agro.inventory.base.BaseViewModel
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    application: Application,
    localRepository: LocalRepository
) : BaseViewModel(application) {
    private val repository = localRepository

    private var _localActivities: MutableLiveData<List<ActivitiesEntity>> = MutableLiveData()
    val localActivities: LiveData<List<ActivitiesEntity>> get() = _localActivities


    fun insertLocalActivities(activitiesEntity: ActivitiesEntity) =
        viewModelScope.launch {
            repository.insertActivitiesLocal(activitiesEntity)
        }

    val getLocalActivitiesAll
        get() = repository
            .getLocalActivitiesAll()
            .asLiveData(viewModelScope.coroutineContext)

    fun getLocalActivities(idPlot: String, pekerjaanId: String) =
        repository.getLocalActivities(idPlot, pekerjaanId)
            .onEach { result ->
                _localActivities.value = result
            }.launchIn(viewModelScope)

    fun getLocalActivitiesByIdJob(idPlot: String,pekerjaanId: String) =
        repository.getLocalActivitiesByIdJob(idPlot, pekerjaanId)
            .onEach { result ->
                _localActivities.value = result
            }.launchIn(viewModelScope)


    fun updateActivities(
        pekerja: String? = null,
        namaPekerjaan: String? = null,
        idPekerjaan: String? = null,
        nameActivity: String? = null,
        lat: String? = null,
        lng: String? = null,
        volume: String? = null,
        satuan: String? = null,
        photo: String? = null,
        workersId: String? = null,
        id: String? = null
    ) =
        viewModelScope.launch {
            repository.updateActivities(
                pekerja,
                namaPekerjaan,
                idPekerjaan,
                nameActivity,
                lat,
                lng,
                volume,
                satuan,
                photo,
                workersId,
                id
            )
        }

    fun insertLocalArea(areaEntity: List<AreaEntity>) =
        viewModelScope.launch {
            repository.insertAreaLocal(areaEntity)
        }

    val getAreaLocal
        get() = repository
            .getLocalArea()
            .asLiveData(viewModelScope.coroutineContext)

    fun updateStatus(
        status: Boolean? = null,
        memberno: String? = null,
        statusDone: Boolean? = null
    ) =
        viewModelScope.launch {
            repository.updateStatusArea(
                status,
                memberno,
                statusDone
            )
        }

    fun deleteAllActivities() = viewModelScope.launch {
        repository.deleteActivities()
    }

    fun deleteAllArea() = viewModelScope.launch {
        repository.deleteArea()
    }
    fun deleteLocalItemActivities(id: Int? = null) =
        viewModelScope.launch {
            repository.deleteLocalItemActivities(id)
        }



}
package com.agro.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agro.inventory.base.BaseViewModel
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity
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


    private var _getInvent: MutableLiveData<List<InventEntity>> = MutableLiveData()
    val getInvent: LiveData<List<InventEntity>> get() = _getInvent
    private var _getReInvent: MutableLiveData<List<ReinventEntity>> = MutableLiveData()
    val getReInvent: LiveData<List<ReinventEntity>> get() = _getReInvent


    fun updateReInvent(
        jmlTanam: String? = null,
        jmlHidup: String? = null,
        jmlSakit: String? = null,
        keliling: String? = null,
        tinggi: String? = null,
        photo: String? = null,
        lat: String? = null,
        lng: String? = null,
        idComodity: Int? = null,
        id: String? = null
    ) =
        viewModelScope.launch {
            repository.updateReInvent(
                jmlTanam,
                jmlHidup,
                jmlSakit,
                keliling,
                tinggi,
                photo,
                lat,
                lng,
                idComodity,
                id
            )
        }

    fun getLocalReInvent(idComodity: String, kodePlot: String) =
        repository.getReInvent(idComodity, kodePlot)
            .onEach { result ->
                _getReInvent.value = result
            }.launchIn(viewModelScope)

    fun updateInvent(
        jmlTanam: String? = null,
        keliling: String? = null,
        tinggi: String? = null,
        idComodity: Int? = null,
        photo: String? = null,
        lat: String? = null,
        lng: String? = null,
        id: String? = null
    ) =
        viewModelScope.launch {
            repository.updateInvent(
                jmlTanam,
                keliling,
                tinggi,
                idComodity,
                photo,
                lat,
                lng,
                id
            )
        }

    fun getLocalInvent(idComodity: String, kodePlot: String) =
        repository.getInvent(idComodity, kodePlot)
            .onEach { result ->
                _getInvent.value = result
            }.launchIn(viewModelScope)


    fun insertLocalInventPlot(inventPlotEntity: List<InventPlotEntity>) =
        viewModelScope.launch {
            repository.insertInventPlotLocal(inventPlotEntity)
        }

    val getInventLocal
        get() = repository
            .getLocalInventPlot()
            .asLiveData(viewModelScope.coroutineContext)

    fun updateStatusInventPlot(
        status: Boolean? = null,
        memberno: String? = null,
        statusDone: Boolean? = null
    ) =
        viewModelScope.launch {
            repository.updateStatusInventPlot(
                status,
                memberno,
                statusDone
            )
        }


    fun deleteAllInventPlot() = viewModelScope.launch {
        repository.deleteInventPlot()
    }

    // input

    fun insertLocalInvent(inventEntity: InventEntity) =
        viewModelScope.launch {
            repository.insertInventLocal(inventEntity)
        }

    fun insertLocalReinvent(reinventEntity: ReinventEntity) =
        viewModelScope.launch {
            repository.insertReInventLocal(reinventEntity)
        }

}
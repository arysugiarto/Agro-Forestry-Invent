package com.agro.inventory.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agro.inventory.base.BaseViewModel
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
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

    private var _getInventAll: MutableLiveData<List<InventEntity>> = MutableLiveData()
    val getInventAll: LiveData<List<InventEntity>> get() = _getInventAll

    private var _getReInvent: MutableLiveData<List<ReinventEntity>> = MutableLiveData()
    val getReInvent: LiveData<List<ReinventEntity>> get() = _getReInvent

    private var _getReInventAll: MutableLiveData<List<ReinventEntity>> = MutableLiveData()
    val getReInventAll: LiveData<List<ReinventEntity>> get() = _getReInventAll


    private var _getInventPlot: MutableLiveData<List<InventPlotEntity>> = MutableLiveData()
    val getInventPlot: LiveData<List<InventPlotEntity>> get() = _getInventPlot
    private var _getReInventPlot: MutableLiveData<List<ReInventPlotEntity>> = MutableLiveData()
    val getReInventPlot: LiveData<List<ReInventPlotEntity>> get() = _getReInventPlot

    private var _getComodity: MutableLiveData<List<ComodityEntity>> = MutableLiveData()
    val getComodity: LiveData<List<ComodityEntity>> get() = _getComodity

    private var _getInventData: MutableLiveData<List<InventDataEntity>> = MutableLiveData()
    val getInventData: LiveData<List<InventDataEntity>> get() = _getInventData


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

    fun getLocalInvent(comodity: String,idComodity: String, kodePlot: String) =
        repository.getInvent(comodity, idComodity, kodePlot)
            .onEach { result ->
                _getInvent.value = result
            }.launchIn(viewModelScope)

    fun getLocalInventAll() =
        repository.getInventAll()
            .onEach { result ->
                _getInventAll.value = result
            }.launchIn(viewModelScope)



    fun insertLocalInventPlot(inventPlotEntity: List<InventPlotEntity>) =
        viewModelScope.launch {
            repository.insertInventPlotLocal(inventPlotEntity)
        }

//    val getInventLocal
//        get() = repository
//            .getLocalInventPlot()
//            .asLiveData(viewModelScope.coroutineContext)

    fun getInventLocal(search: String) =
        repository.getLocalInventPlot(search)
            .onEach { result ->
                _getInventPlot.value = result
            }.launchIn(viewModelScope)


    fun updateStatusInventPlot(
        status: Boolean? = null,
        statusDone: Boolean? = null,
        kodePlot: String? = null

    ) =
        viewModelScope.launch {
            repository.updateStatusInventPlot(
                status,
                statusDone,
                kodePlot,
            )
        }
    fun deleteAllInventPlot() = viewModelScope.launch {
        repository.deleteInventPlot()
    }

    //reinvent

    fun insertLocalReInventPlot(reInventPlotEntity: List<ReInventPlotEntity>) =
        viewModelScope.launch {
            repository.insertReInventPlotLocal(reInventPlotEntity)
        }

//    val getReInventLocal
//        get() = repository
//            .getLocalReInventPlot()
//            .asLiveData(viewModelScope.coroutineContext)

    fun getReInventLocal(search: String) =
        repository.getLocalReInventPlot(search)
            .onEach { result ->
                _getReInventPlot.value = result
            }.launchIn(viewModelScope)

    fun getReInventAll() =
        repository.getReInventAll()
            .onEach { result ->
                _getReInventAll.value = result
            }.launchIn(viewModelScope)


    fun updateStatusReInventPlot(
        status: Boolean?,
        statusDone: Boolean? = null,
        kodePlot: String? = null

    ) =
        viewModelScope.launch {
            repository.updateStatusReInventPlot(
                status,
                statusDone,
                kodePlot
            )
        }


    fun deleteAllReInventPlot() = viewModelScope.launch {
        repository.deleteReInventPlot()
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

    fun insertLocalComodity(comodityEntity: List<ComodityEntity>) =
        viewModelScope.launch {
            repository.insertComodity(comodityEntity)
        }

    fun getLocalComodity(kodePlot: String) =
        repository.getComodity(kodePlot)
            .onEach { result ->
                _getComodity.value = result
            }.launchIn(viewModelScope)

    fun deleteAllComodity() = viewModelScope.launch {
        repository.deleteComodity()
    }

    fun insertLocalInventData(inventDataEntity: List<InventDataEntity>) =
        viewModelScope.launch {
            repository.insertInventData(inventDataEntity)
        }

    fun getLocalInventData(kodePlot: String) =
        repository.getInventData(kodePlot)
            .onEach { result ->
                _getInventData.value = result
            }.launchIn(viewModelScope)

    fun deleteAllInventData() = viewModelScope.launch {
        repository.deleteInventData()
    }

    fun updateStatusComodity(
        status: Boolean?,
        kodePlot: String? = null,
        comodity: String? = null

    ) =
        viewModelScope.launch {
            repository.updateStatusComodity(
                status,
                kodePlot,
                comodity
            )
        }



}
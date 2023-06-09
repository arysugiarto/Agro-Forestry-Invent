package com.agro.inventory.ui.invent.adapter
import com.agro.inventory.base.BaseAdapter
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.remote.model.ActivitiesResponse
import com.agro.inventory.data.remote.model.ListJobResponse
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.WorkerResponse
import com.agro.inventory.data.remote.model.local.AddNameWorker
import com.agro.inventory.databinding.*

import com.agro.inventory.util.textOrNull


object InventAdapter {
    private var comodityList: List<String> = emptyList()

    private var nameWorkerList: List<String> = emptyList()

    val codePlotAdapter  =
        BaseAdapter.adapterOf<ListPlotResponse.Data, ItemPlotBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitlePlot.textOrNull = item.kodePlot
                        tvPolaTanamValue.textOrNull = ":" + item.polaTanamName
                        tvComodityValue.textOrNull = ":" + item.komoditas

                        cvPlot.setOnClickListener {
                            onClickCodePlotCallback.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )

    val workerAdapter  =
        BaseAdapter.adapterOf<ListJobResponse.Data, ItemListBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitle.textOrNull = item.pekerjaanName

                        tvTitle.setOnClickListener {
                            onClickWorkerCallback.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )

    val nameWorkerAdapter  =
        BaseAdapter.adapterOf<WorkerResponse.Data, ItemCheckBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitle.textOrNull = item.name

                        cbCheck.setOnClickListener {
                            val data = AddNameWorker(
                                id = item.id.toString(),
                                name = item.name,
                            )
                            onClickNameWorkerCallback?.invoke(data, cbCheck.isChecked)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )

    val addNameWorkerAdapter =
        BaseAdapter.adapterOf<AddNameWorker, ItemAddInventBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {
                        tvTitle.textOrNull = item.name

                        ibClose.setOnClickListener {
                            onNameWorkerRemoveListener?.invoke(item)
                        }
                    }
                }),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new -> old == new }
            )
        )


    val activitiesAdapter =
        BaseAdapter.adapterOf<ActivitiesResponse.Data, ItemAddInventBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {
                        tvTitle.textOrNull = item.activityName

                        tvTitle.setOnClickListener {
                            onClickActivitiesCallback.invoke(item)
                        }
                    }
                }),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new -> old == new }
            )
        )

    val activitiesLocalAdapter =
        BaseAdapter.adapterOf<ActivitiesEntity, ItemActivityBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitlePlot.textOrNull = item.namaPekerjaan.toString()
                        tvActivities.textOrNull = item.nameActivity.toString()
                        tvVolume.textOrNull = item.volume.toString() + " " + item.satuan

                        ibEdit.setOnClickListener {
                            onClickActivitiesLocalCallback.invoke(item)
                        }
                        ibDelete.setOnClickListener{
                            onClickDeleteActivitiesLocalCallback.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )


    private var onClickCodePlotCallback: (ListPlotResponse.Data) -> Unit = {}

    fun setOnClickCodePlot(item: (ListPlotResponse.Data) -> Unit) {
        onClickCodePlotCallback = item
    }


    private var onClickWorkerCallback: (ListJobResponse.Data) -> Unit = {}

    fun setOnclickWorker(item: (ListJobResponse.Data) -> Unit) {
        onClickWorkerCallback = item
    }


    fun setComodityData(data: List<String>) {
        comodityList = data
    }

    fun setNameWorkerData(data: List<String>) {
        nameWorkerList = data
    }

    private var onClickActivitiesCallback: (ActivitiesResponse.Data) -> Unit = {}

    fun setOnclickActivities(item: (ActivitiesResponse.Data) -> Unit) {
        onClickActivitiesCallback = item
    }

    private var onClickActivitiesLocalCallback: (ActivitiesEntity) -> Unit = {}

    fun setOnclickActivitiesLocal(item: (ActivitiesEntity) -> Unit) {
        onClickActivitiesLocalCallback = item
    }

    private var onClickDeleteActivitiesLocalCallback: (ActivitiesEntity) -> Unit = {}

    fun setOnclickDeleteActivitiesLocal(item: (ActivitiesEntity) -> Unit) {
        onClickDeleteActivitiesLocalCallback = item
    }

    private var onClickNameWorkerCallback: ((AddNameWorker,Boolean) -> Unit)? = null

    fun setOnclickNameWorker(item: (AddNameWorker,Boolean) -> Unit) {
        onClickNameWorkerCallback = item
    }

    private var onNameWorkerRemoveListener: ((AddNameWorker) -> Unit)? =
        null

    fun setOnNameWorkerRemoveClickListener(listener: (AddNameWorker) -> Unit) {
        onNameWorkerRemoveListener = listener
    }


}
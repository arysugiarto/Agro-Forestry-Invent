package com.agro.inventory.ui.invent.adapter

import androidx.core.view.isVisible
import com.agro.inventory.R
import com.agro.inventory.base.BaseAdapter
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.ItemComodityBinding
import com.agro.inventory.databinding.ItemPlotBinding
import com.agro.inventory.util.color
import com.agro.inventory.util.textOrNull


object InventAdapter {

    val codePlotAdapter  =
        BaseAdapter.adapterOf<InventPlotEntity, ItemPlotBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitlePlot.textOrNull = item.kodePlot
                        tvPolaTanam.textOrNull = item.polaTanam
                        tvPolaTanamValue.textOrNull = ":" + " " +item.komoditas

                        btnNext.setOnClickListener {
                            onClickCodePlotCallback.invoke(item)
                        }

                        if (item.statusDone == true){
                            btnNext.text = "Lanjut"
//                            btnNext.setBackgroundColor(root.context.color(R.color.sandy_brown))
                            btnDone.isVisible = true
                        }

                        if (item.status == true && item.statusDone == true){
                            btnNext.isVisible = false
                            btnDone.isVisible = false
                            tvDone.isVisible = true
                        }
                        btnDone.setOnClickListener{
                            onClickDoneCallback.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )


    val cmodityAdapter  =
        BaseAdapter.adapterOf<ComodityEntity, ItemComodityBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvComodity.textOrNull = item.comodity
                        tvPolaTanam.textOrNull = "Monokultur"

                        cvPlot.setOnClickListener {
                            onClickComodityCallback.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )


    private var onClickComodityCallback: (ComodityEntity) -> Unit = {}

    fun setOnClickComodityPlot(item: (ComodityEntity) -> Unit) {
        onClickComodityCallback = item
    }

    private var onClickCodePlotCallback: (InventPlotEntity) -> Unit = {}

    fun setOnClickCodePlot(item: (InventPlotEntity) -> Unit) {
        onClickCodePlotCallback = item
    }

    private var onClickDoneCallback: (InventPlotEntity) -> Unit = {}

    fun setOnClickDone(item: (InventPlotEntity) -> Unit) {
        onClickDoneCallback = item
    }


}
package com.agro.inventory.ui.invent.adapter

import com.agro.inventory.base.BaseAdapter
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.ItemComodityBinding
import com.agro.inventory.databinding.ItemPlotBinding
import com.agro.inventory.util.textOrNull


object ReInventAdapter {

    val codePlotAdapter  =
        BaseAdapter.adapterOf<ListPlotResponse.Data, ItemPlotBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitlePlot.textOrNull = item.kodePlot
                        tvPolaTanamValue.textOrNull = ":" + item.polaTanamName
                        tvComodityValue.textOrNull = ":" + item.komoditas

                        btnNext.setOnClickListener {
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

    val cmodityAdapter  =
        BaseAdapter.adapterOf<Comodity, ItemComodityBinding>(
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



    private var onClickCodePlotCallback: (ListPlotResponse.Data) -> Unit = {}

    fun setOnClickCodePlot(item: (ListPlotResponse.Data) -> Unit) {
        onClickCodePlotCallback = item
    }


    private var onClickComodityCallback: (Comodity) -> Unit = {}

    fun setOnClickComodityPlot(item: (Comodity) -> Unit) {
        onClickComodityCallback = item
    }


}
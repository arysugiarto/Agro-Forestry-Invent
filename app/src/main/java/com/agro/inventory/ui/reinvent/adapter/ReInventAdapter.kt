package com.agro.inventory.ui.invent.adapter

import androidx.core.view.isVisible
import com.agro.inventory.R
import com.agro.inventory.base.BaseAdapter
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.ItemComodityBinding
import com.agro.inventory.databinding.ItemPlotBinding
import com.agro.inventory.util.color
import com.agro.inventory.util.textOrNull


object ReInventAdapter {

    val codePlotAdapter  =
        BaseAdapter.adapterOf<ReInventPlotEntity, ItemPlotBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvTitlePlot.textOrNull = item.kodePlot
                        tvPolaTanamValue.textOrNull = ":" + item.polaTanam
                        tvComodityValue.textOrNull = ":" + item.komoditas

                        btnNext.setOnClickListener {
                            onClickCodePlotCallback.invoke(item)
                        }

                        if (item.status == true){
                            btnNext.text = "Lanjut"
                            btnNext.setBackgroundColor(root.context.color(R.color.sandy_brown))
                            btnDone.isVisible = true
                        }

                        if (item.status == true && item.statusDone == true){
                            btnNext.isVisible = false
                            btnDone.isVisible = false
                            tvDone.isVisible = true
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



    private var onClickCodePlotCallback: (ReInventPlotEntity) -> Unit = {}

    fun setOnClickCodePlot(item: (ReInventPlotEntity) -> Unit) {
        onClickCodePlotCallback = item
    }


    private var onClickComodityCallback: (ComodityEntity) -> Unit = {}

    fun setOnClickComodityPlot(item: (ComodityEntity) -> Unit) {
        onClickComodityCallback = item
    }


}
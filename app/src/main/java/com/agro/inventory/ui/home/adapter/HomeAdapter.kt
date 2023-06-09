package com.agro.inventory.ui.home.adapter
import androidx.core.view.isVisible
import com.agro.inventory.base.BaseAdapter
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.databinding.ItemLandBinding
import com.agro.inventory.util.textOrNull


object HomeAdapter {

    val codePlotAdapter  =
        BaseAdapter.adapterOf<AreaEntity, ItemLandBinding>(
            register = BaseAdapter.Register(
                onBindHolder = { pos, item, view ->
                    view.run {

                        tvLand.textOrNull = item.namearea
                        tvNama.textOrNull = item.nameMember
                        tvKode.textOrNull = item.memberno

                        if (item.status == true){
                            btnNext.text = "Lanjut"
                            btnDone.isVisible = true
                        }else if (item.status == false && item.statusDone == true){
                            btnNext.isVisible = false
                            btnDone.isVisible = false
                            tvDone.isVisible = true
                        }else if (item.status == false && item.statusDone == false){
                            btnNext.text = "Mulai"
                            btnDone.isVisible = false
                            tvDone.isVisible = false
                        }

                        btnNext.setOnClickListener {
                            onClickLandCallback?.invoke(item)
                        }

                        btnDone.setOnClickListener {
                            onClickDoneCallback?.invoke(item)
                        }

                    }
                }
            ),
            diff = BaseAdapter.Diff(
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )

    private var onClickLandCallback: ((AreaEntity) -> Unit)? =
        null

    fun setOnClickLandListener(listener: (AreaEntity) -> Unit) {
        onClickLandCallback = listener
    }

    private var onClickDoneCallback: ((AreaEntity) -> Unit)? =
        null

    fun setOnClickDoneListener(listener: (AreaEntity) -> Unit) {
        onClickDoneCallback = listener
    }

}
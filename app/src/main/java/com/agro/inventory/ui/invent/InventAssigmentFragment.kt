package com.agro.inventory.ui.invent

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.FragmentInventAssigmentBinding
import com.agro.inventory.databinding.LayoutChooseComodityBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.ui.invent.adapter.InventAdapter.setOnClickComodityPlot
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.addDelayOnTypeWithScope
import com.agro.inventory.util.alertDialog
import com.agro.inventory.util.emptyString
import com.agro.inventory.util.hideKeyboard
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
import com.agro.inventory.util.orEmpty
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InventAssigmentFragment : Fragment(R.layout.fragment_invent_assigment) {

    private val binding by viewBinding<FragmentInventAssigmentBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)
    private val viewModels by viewModels<LocalViewModel>()

    private var listComodity = emptyList<Comodity>()
    private val comodityAdapter = InventAdapter.cmodityAdapter


    @Inject
    lateinit var accessManager: AccessManager

    private val kodePlotAdapter = InventAdapter.codePlotAdapter

    var polaTanam = emptyString
    var kodePlot = emptyString
    var idPlot = emptyString
    var komoditas = emptyString

    var keyword = emptyString
    private var item = ListPlotResponse()
    private lateinit var inventPlotEntity: List<InventPlotEntity>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelCallback()
        initOnClick()
        initAdapter()
        initAdapterClick()
        initTextDelayOnType()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

        viewModels.getInventLocal( "ALL")

        var data = emptyList<InventPlotEntity>()
        viewModels.getInventPlot.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            if (data.isEmpty()) {
                binding.ivEmptyState.isVisible = true
                binding.tvEmptyState.isVisible = true
                binding.fab.isVisible = true
                binding.rvPlot.isVisible = false
                binding.label.isVisible = false
            } else {
                binding.ivEmptyState.isVisible = false
                binding.tvEmptyState.isVisible = false
                binding.fab.isVisible = false
                binding.rvPlot.isVisible = true
                binding.label.isVisible = true
            }

        }

    }



    private fun initViewModelCallback() {
        initLocalPlotCallback()
    }

    private fun initLocalPlotCallback(){
        viewModels.getInventLocal("ALL")
        var data = emptyList<InventPlotEntity>()
        viewModels.getInventPlot.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            kodePlotAdapter.items = data
            binding.rvPlot.adapter = kodePlotAdapter

        }
        Timber.e("test%s", data.toString())
    }


    private fun initAdapter() {
        binding.rvPlot.adapter = kodePlotAdapter
        binding.etSearch.setOnEditorActionListener(onImeSearchClicked)
    }

    private fun initPlotCallback() {
        viewModel.plot.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {

                    viewModels.deleteAllInventPlot()
                    binding.fab.isVisible = false

                    item = result.data!!
                    inventPlotEntity = item.data?.map {
                        InventPlotEntity(
                            idPlot = it.id,
                            kodePlot = it.kodePlot,
                            namearea = "Lahan 1",
//                            nameMember = it.memberName,
                            komoditas = it.komoditas,
                            polaTanam = it.polaTanamName,
                            status = false,
                            allData = "ALL"
                        )
                    }.orEmpty()

                    viewModels.insertLocalInventPlot(inventPlotEntity)
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initAdapterClick() {
        InventAdapter.setOnClickCodePlot { item ->
            Timber.e(item.polaTanam)
            if (item.polaTanam.toString() == "Monokultur") {
                navController.navigateOrNull(
                    InventAssigmentFragmentDirections.actionInventAssigmentFragmentToInventFragment(
                        item.id.toString(),
                        item.kodePlot,
                        item.polaTanam,
                        item.komoditas,
                    )
                )
            } else if (item.polaTanam.toString() == "Polikultur") {
                idPlot = item.id.toString()
                kodePlot = item.kodePlot.toString()
                polaTanam = item.polaTanam.toString()
                komoditas = item.komoditas.toString()
                dialogChoosePlot()
            }

        }

    }

    private fun dialogChoosePlot() {
        val dialogBinding = LayoutChooseComodityBinding.inflate(layoutInflater)
        context?.alertDialog(dialogBinding.root)?.apply {
            show()

            listComodity = listOf(
                Comodity(
                    1,
                    "Kopi",
                ),
                Comodity(
                    2,
                    "Vannila",
                )
            )

            dialogBinding.apply {

                comodityAdapter.items = listComodity
                rvComodity.adapter = comodityAdapter

            }
            setOnClickComodityPlot { item ->
                navController.navigateOrNull(
                    InventAssigmentFragmentDirections.actionInventAssigmentFragmentToInventFragment(
                        idPlot,
                        kodePlot,
                        polaTanam,
                        item.comodity,
                        item.id.toString()
                    )
                )
                dismiss()
            }
        }
    }

    private fun initTextDelayOnType() {
        binding.apply {
            boxSearch.editText?.addDelayOnTypeWithScope(200, lifecycleScope) {
                if (etSearch.text?.isNotEmpty().orEmpty) {
                    boxSearch.setEndIconDrawable(R.drawable.ic_clear)
                } else boxSearch.setEndIconDrawable(R.drawable.ic_search)
                boxSearch.setEndIconOnClickListener {
                    etSearch.setText(emptyString)
                    boxSearch.setEndIconDrawable(R.drawable.ic_search)
                    keyword = etSearch.text.toString()
                    initLocalPlotCallback()

                }
            }
        }
    }

    private val onImeSearchClicked = TextView.OnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            val keyword = binding.etSearch.text.toString()
            binding.etSearch.clearFocus()

            if (keyword != emptyString) {

                viewModels.getInventLocal(keyword)

            } else {


            }
            activity?.hideKeyboard(v)

            return@OnEditorActionListener true
        }
        false
    }
    private fun initOnClick() {
        binding.apply {
            tvTitle.setOnClickListener(onClickCallback)
            fab.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.tvTitle -> {
                navController.navigateOrNull(
                    InventAssigmentFragmentDirections.actionInventAssigmentFragmentToHomeFragment()
                )
            }
            binding.fab -> {
                viewModel.requestListPlot(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    "2"
                )
                viewModels.getInventLocal("")
                initLocalPlotCallback()
                initPlotCallback()
            }
        }

    }

}
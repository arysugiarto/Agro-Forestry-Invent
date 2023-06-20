package com.agro.inventory.ui.invent

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agro.inventory.ui.invent.InventAssigmentFragmentDirections
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.AreaEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.AllMonitoringWorkerBodyRequest
import com.agro.inventory.data.remote.model.AreaResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.FragmentInventAssigmentBinding
import com.agro.inventory.databinding.LayoutChooseComodityBinding
import com.agro.inventory.ui.home.adapter.HomeAdapter
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.ui.invent.adapter.InventAdapter.setOnClickComodityPlot
import com.agro.inventory.ui.invent.adapter.ReInventAdapter
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.alertDialog
import com.agro.inventory.util.emptyString
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
import com.agro.inventory.util.textOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViewModelCallback()
        initOnClick()
        initAdapter()
        initAdapterClick()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

    }


    private fun initViewModel() {
        viewModel.requestListPlot(
            "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
            "1550446421",
           "2"
        )
    }

    private fun initViewModelCallback() {
        initPlotCallback()
    }


    private fun initAdapter() {
        binding.rvPlot.adapter = kodePlotAdapter

    }


    private fun initPlotCallback() {
        viewModel.plot.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    kodePlotAdapter.items = result.data?.data.orEmpty()

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initAdapterClick() {
        InventAdapter.setOnClickCodePlot { item ->
            Timber.e(item.polaTanam)
            if (item.polaTanamName.toString() == "Monokultur") {
                navController.navigateOrNull(
                    InventAssigmentFragmentDirections.actionInventAssigmentFragmentToInventFragment(
                        item.id.toString(),
                        item.kodePlot,
                        item.polaTanamName,
                        item.komoditas,
                    )
                )
            } else if (item.polaTanamName.toString() == "Polikultur") {
                idPlot = item.id.toString()
                kodePlot = item.kodePlot.toString()
                polaTanam = item.polaTanamName.toString()
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

            }
        }

    }

}
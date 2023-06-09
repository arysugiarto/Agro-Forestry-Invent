package com.agro.inventory.ui.invent

//import com.arysugiarto.attendence.ui.main.MainFragment.Companion.parentBottomAppBar

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.agro.inventory.ui.invent.PlotFragmentArgs
import com.agro.inventory.ui.invent.PlotFragmentDirections
import com.agro.inventory.R
import com.agro.inventory.data.remote.Result
import com.agro.inventory.databinding.FragmentPlotBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
import com.agro.inventory.util.textOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlotFragment : Fragment(R.layout.fragment_plot) {

    private val binding by viewBinding<FragmentPlotBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)

    private val args by navArgs<PlotFragmentArgs>()

    private val kodePlotAdapter = InventAdapter.codePlotAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViewModelCallback()
        initOnClick()
        initAdapter()
        initAdapterClick()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false


        viewModel.getAreaDataStore()
        viewModel.getNameMemberDataStore()
        viewModel.getNoMemberDataStore()
        viewModel.getPlotDataStore()

        viewModel.areaDatastore.observe(viewLifecycleOwner) {
            binding.tvNameAreaValue.textOrNull = ": $it"

        }
        viewModel.nameMemberDatastore.observe(viewLifecycleOwner) {
            binding.tvNameMemberValue.textOrNull  = ": $it"

        }
        viewModel.noMemberDatastore.observe(viewLifecycleOwner) {
            binding.labelKodeValue.textOrNull = ": $it"

        }
        viewModel.plotDatastore.observe(viewLifecycleOwner) {
            binding.tvPlotCountValue.textOrNull = ": $it"

        }

    }


    private fun initViewModel() {
        viewModel.requestListPlot(
            "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
            "1550446421",
            args.areaId.toString()
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
                    kodePlotAdapter.items =
                        result.data?.data.orEmpty()

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initAdapterClick() {
        InventAdapter.setOnClickCodePlot { item ->
            navController.navigateOrNull(
                PlotFragmentDirections.actionKodePlotFragmentToComodityFragment(
//                    item.id.toString(),
//                    item.kodePlot,
//                    item.polaTanamName,
//                    item.komoditas,

                )
            )
        }

    }

    private fun initOnClick() {
        binding.apply {
            tvTitle.setOnClickListener(onClickCallback)

        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.tvTitle -> {
                navController.navigateOrNull(
                    PlotFragmentDirections.actionKodePlotFragmentToInventAssigmentFragment()
                )
            }
        }

    }

}
package com.agro.inventory.ui.reinvent

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.agro.inventory.R
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.FragmentReinventAssigmentBinding
import com.agro.inventory.databinding.LayoutChooseComodityBinding
import com.agro.inventory.ui.invent.adapter.ReInventAdapter
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickCodePlot
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickComodityPlot
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.alertDialog
import com.agro.inventory.util.emptyString
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReInventAssigmentFragment : Fragment(R.layout.fragment_reinvent_assigment) {

    private val binding by viewBinding<FragmentReinventAssigmentBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)
    private val viewModels by viewModels<LocalViewModel>()

    @Inject
    lateinit var accessManager: AccessManager

    private val kodePlotAdapter = ReInventAdapter.codePlotAdapter
    private var listComodity = emptyList<Comodity>()
    private val comodityAdapter = ReInventAdapter.cmodityAdapter

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
        binding.rvLand.adapter = kodePlotAdapter
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
        setOnClickCodePlot { item ->
            if (item.polaTanamName.toString() == "Monokultur") {
                navController.navigateOrNull(
                    ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
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
                    ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
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
                    ReInventAssigmentFragmentDirections.actionInventAssigmentFragmentToHomeFragment()
                )
            }
            binding.fab -> {


            }
        }

    }

}
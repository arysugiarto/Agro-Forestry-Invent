package com.agro.inventory.ui.invent

//import com.arysugiarto.attendence.ui.main.MainFragment.Companion.parentBottomAppBar

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.agro.inventory.ui.invent.ComodityFragmentDirections
import com.agro.inventory.R
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.databinding.FragmentComodityBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComodityFragment : Fragment(R.layout.fragment_comodity) {

    private val binding by viewBinding<FragmentComodityBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)

    private var listComodity = emptyList<Comodity>()
    private val comodityAdapter = InventAdapter.cmodityAdapter

    private val args by navArgs<ComodityFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initOnClick()
        initAdapter()
        initClickAdapter()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false


        viewModel.getAreaDataStore()
        viewModel.getNameMemberDataStore()
        viewModel.getNoMemberDataStore()
        viewModel.getPlotDataStore()

    }


    private fun initView() {
        listStaticData()
    }

    private fun listStaticData() {
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

    }

    private fun initAdapter() {
        comodityAdapter.items = listComodity
        binding.rvComodity.adapter = comodityAdapter
    }

    private fun initClickAdapter() {
        InventAdapter.setOnClickComodityPlot { item ->
            navController.navigateOrNull(
                ComodityFragmentDirections.actionComodityFragmentToInventFragment(
                    args.idPlot,
                    args.kodePlot,
                    args.polaTanam,
                    item.comodity,
                    item.id.toString()
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
                    ComodityFragmentDirections.actionComodityFragmentToInventFragment()
                )
            }
        }

    }

}
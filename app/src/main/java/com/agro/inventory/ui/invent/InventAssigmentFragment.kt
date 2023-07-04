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
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.data.remote.model.invent.TaskPlotResponse
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
import com.agro.inventory.viewmodel.AuthViewModel
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
    private val viewModelsAuth by viewModels<AuthViewModel>()
    private val args by navArgs<InventAssigmentFragmentArgs>()

    private var listComodity = emptyList<Comodity>()
    private val comodityAdapter = InventAdapter.cmodityAdapter
    private var saveInvent = listOf<SaveInventBodyRequest.Data>()


    @Inject
    lateinit var accessManager: AccessManager

    private val kodePlotAdapter = InventAdapter.codePlotAdapter

    var polaTanam = emptyString
    var kodePlot = emptyString
    var idPlot = emptyString
    var komoditas = emptyString
    var userAccessId = emptyString

    var keyword = emptyString
    private var item = TaskPlotResponse()
    private lateinit var inventPlotEntity: List<InventPlotEntity>
    private lateinit var inventEntity: List<InventEntity>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelCallback()
        initOnClick()
        initAdapter()
        initAdapterClick()
        initTextDelayOnType()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

        viewModelsAuth.getUserAccessId()
        viewModels.getInventLocal( "ALL")
        viewModels.getLocalInventAll()


        viewModelsAuth.useraccess.observe(viewLifecycleOwner) {
            userAccessId = it

            Timber.e(userAccessId)

        }

        Timber.e(args.userAccessId)


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

        var dataInvent = emptyList<InventEntity>()
        viewModels.getInventAll.observe(viewLifecycleOwner) { result ->
            dataInvent = result.orEmpty()
            binding.ivDot.isVisible = data.isNotEmpty()
            binding.ivUpload.isVisible = data.isNotEmpty()

            saveInvent =
                dataInvent.map {
                    SaveInventBodyRequest.Data(
                        SaveInventBodyRequest.Data.Plants(
                            plotId = it.idPlot.orEmpty,
                            plantNumber = 1,
                            totalPlant = it.jmlTanam?.toInt()!!,
                            komoditasId = 1,
                            keliling = it.keliling?.toInt()!!,
                            length = it.tinggi?.toInt()!!,
                            userId = 2306,
                            lat = it.lat.toString(),
                            lng = it.lng.toString(),
                            uid ="f48666f9-f85a-461f-befb-7b03bdab2e44" ,
                            appSource = "12",
                            createdBy = 206,
                            deleted = 0
                        ),
                        images = it.photo.toString()

                    )
                }
            Timber.e(saveInvent.toString())

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
        viewModel.taskPlot.observe(viewLifecycleOwner, EventObserver { result ->
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
                            nameMember = it.memberName,
                            komoditas = it.komoditas,
                            polaTanam = "Polikultur",
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

        InventAdapter.setOnClickDone { item ->
            viewModels.updateStatusInventPlot(true,true, item.kodePlot)
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

    private fun initSaveInvent() {
        viewModel.saveInventAll.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
//                    binding.progressBar.isVisible = true
//                    binding.tvProgress.isVisible = true
                }

                is Result.Success -> {
//                    binding.progressBar.isVisible = false
//                    binding.tvProgress.isVisible = false

                    Timber.e("Berhasil")
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success))
                        .setContentText(context?.getString(R.string.register_employee))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
//                            viewModels.deleteAllActivities()
//                            viewModels.deleteAllArea()
//
//                            initAreaLocalCallback()

                        }
                        .show()

                    viewModel.setSaveAllInventNothing()
                }

                is Result.Error -> {

                }

                else -> Unit
            }
        })

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
            ivUpload.setOnClickListener(onClickCallback)
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

                viewModelsAuth.getUserAccessId()

                viewModelsAuth.useraccess.observe(viewLifecycleOwner) {
                    userAccessId = it

                    Timber.e(userAccessId)

                }

                viewModel.requestTaskPlot(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
//                    userAccessId
                "2311"
                )
                viewModels.getInventLocal("")
                initLocalPlotCallback()
                initPlotCallback()

            }

            binding.ivUpload -> {
                initSaveInvent()
                viewModel.requestSaveInventAll(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    saveInvent
                )

                viewModel.setSaveAllInventNothing()
//                Timber.e(dataActivities.toString())

            }
        }

    }

}
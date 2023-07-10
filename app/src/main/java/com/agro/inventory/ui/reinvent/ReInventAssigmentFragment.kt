package com.agro.inventory.ui.reinvent

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.reinvent.InventDataResponse
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import com.agro.inventory.databinding.FragmentReinventAssigmentBinding
import com.agro.inventory.databinding.LayoutChooseComodityBinding
import com.agro.inventory.ui.invent.adapter.ReInventAdapter
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickCodePlot
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickComodityPlot
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
import com.agro.inventory.util.textOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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

    private var saveReInvent = listOf<SaveReinventBodyRequest.Data>()


    var polaTanam = emptyString
    var kodePlot = emptyString
    var idPlot = emptyString
    var komoditas = emptyString

    var keyword = emptyString

    private var item = ListPlotResponse()
    private var itemInventData = InventDataResponse()
    private lateinit var reInventPlotEntity: List<ReInventPlotEntity>
    private lateinit var reInventDataEntity: List<InventDataEntity>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModelCallback()
        initOnClick()
        initAdapter()
        initAdapterClick()
        initTextDelayOnType()
        initInvent()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

        viewModels.getReInventLocal("ALL")
        viewModels.getReInventAll()


        var data = emptyList<ReInventPlotEntity>()
        viewModels.getReInventPlot.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            if (data.isEmpty()) {
                binding.ivEmptyState.isVisible = true
                binding.tvEmptyState.isVisible = true
                binding.fab.isVisible = true
                binding.rvLand.isVisible = false
                binding.label.isVisible = false
            } else {
                binding.ivEmptyState.isVisible = false
                binding.tvEmptyState.isVisible = false
                binding.fab.isVisible = false
                binding.rvLand.isVisible = true
                binding.label.isVisible = true
            }

        }

        var dataReInvent = emptyList<ReinventEntity>()
        viewModels.getReInventAll.observe(viewLifecycleOwner) { result ->
            dataReInvent = result.orEmpty()
            binding.ivDot.isVisible = data.isNotEmpty()
            binding.ivUpload.isVisible = data.isNotEmpty()

            saveReInvent =
                dataReInvent.map {
                    SaveReinventBodyRequest.Data(
                        SaveReinventBodyRequest.Data.Plants(
                            id = it.id?.toInt()!!,
                            plotId = it.idPlot.orEmpty,
                            plantNumber = 1,
                            totalPlant = it.jmlTanam?.toInt().orEmpty,
                            alivesTotal = it.jmlHidup?.toInt().orEmpty,
                            diseasedTrees = it.jmlSakit?.toInt().orEmpty,
                            penyulamanTotal = it.penyulaman?.toInt().orEmpty,
                            komoditasId = 1,
                            keliling = it.keliling?.toInt().orEmpty,
                            length = it.tinggi?.toInt().orEmpty,
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
            Timber.e(saveReInvent.toString())

        }

    }

    private fun initInvent() {
        viewModel.inventData.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false

                }

                is Result.Error -> {

                }

                else -> Unit
            }
        })

    }

    private fun initViewModelCallback() {
        initLocalPlotCallback()
    }

    private fun initLocalPlotCallback(){
        viewModels.getReInventLocal("ALL")
        var data = emptyList<ReInventPlotEntity>()
        viewModels.getReInventPlot.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            kodePlotAdapter.items = data
            binding.rvLand.adapter = kodePlotAdapter

        }
    }


    private fun initAdapter() {
        binding.rvLand.adapter = kodePlotAdapter
        binding.etSearch.setOnEditorActionListener(onImeSearchClicked)
    }


    private fun initPlotCallback() {
        viewModel.plot.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    binding.progressBar.isVisible= false
                    viewModels.deleteAllReInventPlot()
//                    binding.fab.isVisible = false

                    item = result.data!!
                    reInventPlotEntity = item.data?.map {
                        ReInventPlotEntity(
                            idPlot = it.id,
                            kodePlot = it.kodePlot,
                            namearea = "Lahan 1",
                            komoditas = it.komoditas,
                            polaTanam = it.polaTanamName,
                            status = false,
                            allData = "ALL"
                        )
                    }.orEmpty()

                    viewModels.insertLocalReInventPlot(reInventPlotEntity)

                    Timber.e(reInventPlotEntity.toString())

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initInventDataCallback() {
        viewModel.inventData.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    viewModels.deleteAllInventData()
//                    binding.fab.isVisible = false

                    Timber.e("test aja ini mah")

                    itemInventData = result.data!!
                    reInventDataEntity = itemInventData.data?.map {
                        InventDataEntity(
//                            idPlot = it.id,
                            plantNumber= it.plantNumber,
                            komoditas = it.komoditas,
                            totalPlant = it.totalPlant,
                            keliling = it.diameter.toString(),
                            length = it.length,
                            alivesTotal = it.alivesTotal,
                            diesTotal = it.diesTotal,
                            diseasedTrees = it.diseasedTrees,
                            penyulamanTotal = it.penyulamanTotal.toString(),
                            lat = it.lat,
                            lng = it.lng,
                            photo = it.photo,
                            kodePlot = "02-GMF-P"
                        )
                    }.orEmpty()

                    viewModels.insertLocalInventData(reInventDataEntity)

                    Timber.e("invent data%s", reInventDataEntity.toString())

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initAdapterClick() {
        setOnClickCodePlot { item ->
            if (item.polaTanam.toString() == "Monokultur") {
                navController.navigateOrNull(
                    ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
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

    private fun initSaveReInvent() {
        viewModel.saveReInventAll.observe(viewLifecycleOwner, EventObserver { result ->
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

                viewModels.getReInventLocal(keyword)

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
                    ReInventAssigmentFragmentDirections.actionInventAssigmentFragmentToHomeFragment()
                )
            }
            binding.fab -> {
                viewModel.requestListPlot(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    "2"
                )

                viewModel.requestInventData(
                    "Sobi+Apps:11fbbd445c65d9a7f1c2b53ec88ba993",
                    "1550471710",
                    "2311"
                )

                initInventDataCallback()
                initPlotCallback()

            }

            binding.ivUpload -> {
                initSaveReInvent()
                viewModel.requestSaveReInventAll(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    saveReInvent
                )

                viewModel.setSaveAllReInventNothing()

            }
        }

    }

}
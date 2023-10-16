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
import com.agro.inventory.data.local.entity.AuthEntity
import com.agro.inventory.data.local.entity.ComodityEntity
import com.agro.inventory.data.local.entity.InventDataEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.local.entity.ReInventPlotEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.ListPlotResponse
import com.agro.inventory.data.remote.model.RemovePenugasanBodyRequest
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.TaskPlotReinventResponse
import com.agro.inventory.data.remote.model.reinvent.InventDataResponse
import com.agro.inventory.data.remote.model.reinvent.SaveReinventBodyRequest
import com.agro.inventory.databinding.FragmentReinventAssigmentBinding
import com.agro.inventory.databinding.LayoutChooseComodityBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.ui.invent.adapter.ReInventAdapter
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickCodePlot
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickComodityPlot
import com.agro.inventory.ui.invent.adapter.ReInventAdapter.setOnClickDone
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.addDelayOnTypeWithScope
import com.agro.inventory.util.alertDialog
import com.agro.inventory.util.emptyInt
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
    private var listComodity = emptyList<ComodityEntity>()
    private val comodityAdapter = ReInventAdapter.cmodityAdapter

    private var saveReInvent = listOf<SaveReinventBodyRequest.Data>()
    private var remove = listOf<RemovePenugasanBodyRequest.Data>()


    var polaTanam = emptyString
    var kodePlot = emptyString
    var idPlot = emptyString
    var komoditas = emptyString
    var idComodity = emptyString
    var userAccessId = emptyInt
    var keyword = emptyString

    private var item = TaskPlotReinventResponse()
    private var itemInventData = InventDataResponse()
    private lateinit var reInventPlotEntity: List<ReInventPlotEntity>
    private lateinit var reInventDataEntity: List<ReinventEntity>


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
        viewModels.getReInventLocalByStatus(true)

        var dataUser = emptyList<AuthEntity>()
        viewModels.getAuth.observe(viewLifecycleOwner) { result ->
            dataUser = result.orEmpty()
            userAccessId = dataUser.firstOrNull()?.userAccessId.orEmpty

        }

        var data = emptyList<ReInventPlotEntity>()
        viewModels.getReInventPlot.observe(viewLifecycleOwner) { result ->
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

        var dataReInvent = emptyList<ReinventEntity>()
        viewModels.getReInventAll.observe(viewLifecycleOwner) { result ->
            dataReInvent = result.orEmpty()
            binding.ivDot.isVisible = data.isNotEmpty()
            binding.ivUpload.isVisible = data.isNotEmpty()
            binding.tvStatusUpload.isVisible = data.isNotEmpty()

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
                            countReinvent = it.reinventPhase.orEmpty,
                            komoditasId = it.idComodity?.toInt().orEmpty,
                            keliling = it.keliling.orEmpty,
                            length = it.tinggi?.toDouble().orEmpty,
                            userId = userAccessId.toInt(),
                            lat = it.lat.toString(),
                            lng = it.lng.toString(),
                            uid = "f48666f9-f85a-461f-befb-7b03bdab2e44",
                            appSource = "12",
                            createdBy = 206,
                            deleted = 0
                        ),
                        images = it.photo.toString()

                    )
                }
            Timber.e(saveReInvent.toString())

        }

        var dataPlotDone = emptyList<ReInventPlotEntity>()
        viewModels.getReInventPlotByStatus.observe(viewLifecycleOwner) { result ->
            dataPlotDone = result.orEmpty()

            remove = dataPlotDone.map {
                RemovePenugasanBodyRequest.Data(
                    RemovePenugasanBodyRequest.Data.Penugasan(
                        id = it.penugasanId
                    )
                )
            }
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

    private fun initLocalPlotCallback() {
        viewModels.getReInventLocal("ALL")
        var data = emptyList<ReInventPlotEntity>()
        viewModels.getReInventPlot.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            kodePlotAdapter.items = data
            binding.rvPlot.adapter = kodePlotAdapter

        }
    }


    private fun initAdapter() {
        binding.rvPlot.adapter = kodePlotAdapter
        binding.etSearch.setOnEditorActionListener(onImeSearchClicked)
    }


    private fun initPlotCallback() {
        viewModel.taskPlotReinvent.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    viewModels.deleteAllReInventPlot()
//                    binding.fab.isVisible = false

                    item = result.data!!
                    reInventPlotEntity = item.data?.map {
                        ReInventPlotEntity(
                            idPlot = it.id,
                            kodePlot = it.kodePlot,
                            namearea = it.areaName,
                            komoditas = it.komoditas,
                            polaTanam = it.polaTanam,
                            idKomoditas = it.komoditasId,
                            penugasanId = it.penugasanId,
                            status = false,
                            allData = "ALL"
                        )
                    }.orEmpty()

                    viewModels.insertLocalReInventPlot(reInventPlotEntity)

                    Timber.e(reInventPlotEntity.toString())

                }

                is Result.Error<*> -> {
                    binding.progressBar.isVisible = false

                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.warning))
                        .setContentText(context?.getString(R.string.not_data))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()

                        }
                        .show()
                }
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

                    itemInventData = result.data!!
                    reInventDataEntity = itemInventData.data?.map {
                        ReinventEntity(
                            idPlot = it.plotId,
                            plantNumber = it.plantNumber,
                            reinventPhase = it.jumlahReinvent,
                            comodity = it.komoditas,
                            jmlTanam = it.totalPlant.toString(),
                            keliling = it.diameter.toString(),
                            tinggi = it.length.toString(),
                            jmlHidup = it.alivesTotal.toString(),
                            jmlMati = it.diesTotal.toString(),
                            jmlSakit = it.diseasedTrees.toString(),
                            penyulaman = it.penyulamanTotal.toString(),
                            lat = it.lat,
                            lng = it.lng,
                            photo = it.photo,
                            kodePlot = it.kodePlot,
                            idComodity = it.komoditasId.toString()
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

    private fun initRemoveAssigment() {
        viewModel.removeAssigment.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
                }

                is Result.Success -> {

                    Timber.e("Berhasil")
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success))
                        .setContentText(context?.getString(R.string.delete_assigment))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()

                        }
                        .show()

                    viewModel.setRemoveAssigmentNothing()
                }

                is Result.Error -> {

                }

                else -> Unit
            }
        })

    }

    private fun initAdapterClick() {
        setOnClickCodePlot { item ->
            if (item.polaTanam.toString() == "Monokultur" || item.polaTanam.toString() == "Nursery") {
                navController.navigateOrNull(
                    ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
                        idPlot = item.idPlot.toString(),
                        kodePlot = item.kodePlot,
                        polaTanam = item.polaTanam,
                        komoditas = item.komoditas,
                        idKomoditas = item.idKomoditas.toString()
                    )
                )

            } else if (item.polaTanam.toString() == "Polikultur") {
                idPlot = item.idPlot.toString()
                kodePlot = item.kodePlot.toString()
                polaTanam = item.polaTanam.toString()
                komoditas = item.komoditas.toString()
                dialogChoosePlot()
            }
        }


        setOnClickDone { item ->
//            viewModels.deleteLocalItemReInventPlot(item.id)
            viewModels.updateStatusReInventPlot(true, true, item.kodePlot)
            viewModels.updateStatusReInvent(true, item.kodePlot)


        }
    }

    private fun dialogChoosePlot() {
        val dialogBinding = LayoutChooseComodityBinding.inflate(layoutInflater)
        context?.alertDialog(dialogBinding.root)?.apply {
            show()

            Timber.e(kodePlot)
            viewModels.getLocalComodity(kodePlot)
            var data = emptyList<ComodityEntity>()
            viewModels.getComodity.observe(viewLifecycleOwner) { result ->
                data = result.orEmpty()
                comodityAdapter.items = data
                dialogBinding.rvComodity.adapter = comodityAdapter

                Timber.e(data.toString())

            }

            dialogBinding.apply {

                comodityAdapter.items = listComodity
                rvComodity.adapter = comodityAdapter

            }
            setOnClickComodityPlot { item ->
                navController.navigateOrNull(
                    ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
                        idPlot = idPlot,
                        kodePlot = kodePlot,
                        polaTanam = polaTanam,
                        komoditas = item.comodity,
                        idKomoditas = item.idComodity.toString()
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
                    binding.progressBar.isVisible = true
//                    binding.tvProgress.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
//                    binding.tvProgress.isVisible = false

                    Timber.e("Berhasil")
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success))
                        .setContentText(context?.getString(R.string.register_reinvent))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()

                            viewModels.deleteLocalItemReInventPlot(true)
                            viewModels.deleteLocalItemReInvent(true)

                            initRemoveAssigment()

                            viewModel.requestRemoveAssigment(
                                "Sobi+Apps:11fbbd445c65d9a7f1c2b53ec88ba993",
                                "1550471710",
                                remove
                            )

                        }
                        .show()

                    viewModel.setSaveAllReInventNothing()
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

                var data = emptyList<AuthEntity>()
                viewModels.getAuth.observe(viewLifecycleOwner) { result ->
                    data = result.orEmpty()
                    userAccessId = data.firstOrNull()?.userAccessId.orEmpty


                    viewModel.requestComodity(
                        "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                        "1550446421",
                        userAccessId.toString()
                    )

                    viewModel.requestTaskPlotReinvent(
                        "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                        "1550446421",
                        userAccessId.toString()
                    )

                    viewModel.requestInventData(
                        "Sobi+Apps:11fbbd445c65d9a7f1c2b53ec88ba993",
                        "1550471710",
                        userAccessId.toString()
                    )

                }

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
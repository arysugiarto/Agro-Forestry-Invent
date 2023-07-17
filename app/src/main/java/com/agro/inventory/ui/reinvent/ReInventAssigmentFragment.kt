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
import com.agro.inventory.data.remote.model.invent.Comodity
import com.agro.inventory.data.remote.model.invent.SaveInventBodyRequest
import com.agro.inventory.data.remote.model.invent.TaskPlotReinventResponse
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
    private var listComodity = emptyList<ComodityEntity>()
    private val comodityAdapter = ReInventAdapter.cmodityAdapter

    private var saveReInvent = listOf<SaveReinventBodyRequest.Data>()


    var polaTanam = emptyString
    var kodePlot = emptyString
    var idPlot = emptyString
    var komoditas = emptyString
    var idComodity = emptyString
    var userAccessId = emptyString
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

        var dataUser = emptyList<AuthEntity>()
        viewModels.getAuth.observe(viewLifecycleOwner) { result ->
            dataUser = result.orEmpty()
            userAccessId = dataUser.firstOrNull()?.userAccessId.toString()

        }

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
                            keliling = it.keliling.orEmpty,
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
        viewModel.taskPlotReinvent.observe(viewLifecycleOwner, EventObserver { result ->
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
                            polaTanam = it.polaTanam,
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
                        ReinventEntity(
//                            idPlot = it.id,
                            plantNumber= it.plantNumber,
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
                            kodePlot = it.kodePlot
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
            if (item.polaTanam.toString() == "Monokultur"|| item.polaTanam.toString() == "Nursery") {
                viewModels.getLocalComodity("00-GML-N")
                var data = emptyList<ComodityEntity>()
                viewModels.getComodity.observe(viewLifecycleOwner) { result ->
                    data = result.orEmpty()
                    idPlot = data.firstOrNull()?.id.toString()
                    kodePlot = data.firstOrNull()?.kodePlot.toString()
                    polaTanam = item.polaTanam.toString()
                    idComodity = data.firstOrNull()?.idComodity.toString()
                    komoditas = data.firstOrNull()?.comodity.toString()

                    navController.navigateOrNull(
                        ReInventAssigmentFragmentDirections.actionReInventAssigmentFragmentToReinventFragment(
                            idPlot = item.id.toString(),
                            kodePlot = item.kodePlot,
                            polaTanam = item.polaTanam,
                            komoditas = item.komoditas,
                            idKomoditas = "1"
                        )
                    )

                }

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
//                    binding.progressBar.isVisible = true
//                    binding.tvProgress.isVisible = true
                }

                is Result.Success -> {
//                    binding.progressBar.isVisible = false
//                    binding.tvProgress.isVisible = false

                    Timber.e("Berhasil")
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success))
                        .setContentText(context?.getString(R.string.register_reinvent))
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

                var data = emptyList<AuthEntity>()
                viewModels.getAuth.observe(viewLifecycleOwner) { result ->
                    data = result.orEmpty()
                    userAccessId = data.firstOrNull()?.userAccessId.toString()


                    viewModel.requestComodity(
                        "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                        "1550446421",
                        userAccessId
                    )

                    viewModel.requestTaskPlotReinvent(
                        "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                        "1550446421",
                        userAccessId
                    )

                    viewModel.requestInventData(
                        "Sobi+Apps:11fbbd445c65d9a7f1c2b53ec88ba993",
                        "1550471710",
                        userAccessId
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
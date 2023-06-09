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
import com.agro.inventory.databinding.FragmentInventAssigmentBinding
import com.agro.inventory.ui.home.adapter.HomeAdapter
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.emptyString
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.navController
import com.agro.inventory.util.navigateOrNull
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

    @Inject
    lateinit var accessManager: AccessManager

    private val landAdapter = HomeAdapter.codePlotAdapter
    var memberNo = emptyString

    private var dataActivities = listOf<AllMonitoringWorkerBodyRequest.Data>()
    private var item = AreaResponse()
    private lateinit var area: List<AreaEntity>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initCallback()
        initViewModelCallback()
        initOnClick()
        initAdapterClick()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

    }

    private fun initViewModel() {
        var data = emptyList<ActivitiesEntity>()
        viewModels.getLocalActivitiesAll.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()



            dataActivities =
                data.map {
                    AllMonitoringWorkerBodyRequest.Data(
                        AllMonitoringWorkerBodyRequest.Data.Monitoring(
                            idPlot = it.idPlot,
                            workersId = it.workersId.toString(),
                            pekerjaanId = it.pekerjaanId,
                            activityId = it.activityId,
                            volume = it.volume,
                            photo = it.photo,
                            lat = it.lat,
                            lng = it.lng,
                            uid = it.uid,
                            appSource = it.appSource,
                            createdBy = it.createdBy,
                            deleted = it.deleted

                        )

                    )
                }
            Timber.e(dataActivities?.firstOrNull()?.monitoring?.workersId)

        }
    }

    private fun initViewModelCallback() {

        var data = emptyList<AreaEntity>()
        viewModels.getAreaLocal.observe(viewLifecycleOwner) { result ->
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

        viewModel.requestSaveMonitoringWorkerAll(
            "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
            "1550446421",
            dataActivities
        )

        Timber.e(dataActivities.toString())
    }

    private fun initCallback() {
        initAreaLocalCallback()
    }

    private fun iniSaveMonitoringWorker() {
        viewModel.saveMonitoringWorkerAll.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    Timber.e("Berhasil")
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success))
                        .setContentText(context?.getString(R.string.register_employee))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            viewModels.deleteAllActivities()
                            viewModels.deleteAllArea()

                            initAreaLocalCallback()

                        }
                        .show()

                    viewModel.setSaveAllMonitoringNothing()
                }
                is Result.Error -> {

                }
                else -> Unit
            }
        })

    }

    private fun initAreaCallback() =
        viewModel.area.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    viewModels.deleteAllArea()

                    item = result.data!!
                    area = item.data?.map {
                        AreaEntity(
                            areaId = it.areaId,
                            memberno = it.memberNo,
                            namearea = it.namaLahan,
                            nameMember = it.memberName,
                            plotCount = it.jumlahPlot,
                            status = false
                        )
                    }.orEmpty()

                    viewModels.insertLocalArea(area)
                    Timber.e(area.toString())

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }


    private fun initAreaLocalCallback() {
        var data = emptyList<AreaEntity>()
        viewModels.getAreaLocal.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()
            landAdapter.items = data
            binding.rvLand.adapter = landAdapter

        }
    }

    private fun initAdapterClick() {
        HomeAdapter.setOnClickLandListener { item ->
            memberNo = item.memberno.toString()

            lifecycleScope.launch {
                accessManager.setAreaAccess(
                    item.namearea.toString()
                )
                accessManager.setNameMemberAccess(
                    item.nameMember.toString(),
                )
                accessManager.setNoMemberAccess(
                    item.memberno.toString(),
                )
                accessManager.setPlotAccess(
                    item.plotCount.toString()
                )
            }

            navController.navigateOrNull(
                InventAssigmentFragmentDirections.actionHomeFragmentToKodePlotFragment(
                    item.areaId.toString(),
                    item.namearea,
                    item.memberno,
                    item.nameMember,
                    item.plotCount
                )
            )
        }

        HomeAdapter.setOnClickDoneListener { item ->
            viewModels.updateStatus(
                status = false,
                memberno = item.memberno,
                statusDone = true
            )
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

            }
            binding.fab -> {

                viewModel.requestArea(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    "2303",
                    "0"
                )

                initAreaCallback()
            }
        }

    }

}
package com.agro.inventory.ui.invent.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.agro.inventory.R
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.local.AddNameWorker
import com.agro.inventory.data.remote.model.local.NameWorkerSavedStateModel
import com.agro.inventory.databinding.LayoutBottomSheetComodityBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.util.textOrNull
import com.agro.inventory.util.viewBinding
import com.agro.inventory.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ChooseNameWorkerBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding<LayoutBottomSheetComodityBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)

    private val nameWorkerAdapter = InventAdapter.nameWorkerAdapter

    private var nameWorkerList = ArrayList<AddNameWorker>()
    private var nameWorkerChecked = ArrayList<AddNameWorker>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return LayoutInflater.from(context).inflate(
            R.layout.layout_bottom_sheet_comodity,
            container,
            false
        )
    }

    override fun getTheme() = R.style.BottomSheetThemeTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initOnClick()
        initAdapter()
        initClickAdapter()
        initWorkerSavedState()

        nameWorkerList.clear()

    }

    private fun initView() {
        initCallback()
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        viewModel.requestWorker(
            "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
            "1550446421",
            "2303"
        )
    }

    private fun initCallback() {
        initListWorkerCallback()
    }

    private fun initAdapter() {
        binding.rView.adapter = nameWorkerAdapter
    }


    private fun initListWorkerCallback() {
        viewModel.worker.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    nameWorkerAdapter.items = result.data?.data.orEmpty()
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun initClickAdapter() {
        InventAdapter.setOnclickNameWorker{ data, checked ->
            if (checked) {
                nameWorkerList.add(data)
                nameWorkerChecked.add(data)
            } else {
                nameWorkerList.remove(data)
                nameWorkerChecked.remove(data)
            }

            if (nameWorkerChecked.isNotEmpty()) {
                binding.clButton.isVisible = true
                binding.btnAdd.textOrNull = "${context?.getString(R.string.add_worker_multiple)}" +
                        " (${nameWorkerChecked.size})"
            } else binding.clButton.isVisible = false
        }
    }

    private fun initWorkerSavedState() {
        nameWorkerList.clear()

        if (viewModel.nameWorkerSavedState.data.isNotEmpty()) {
            viewModel.nameWorkerSavedState.data.map {
                nameWorkerList.add(it)
            }
            InventAdapter.setNameWorkerData(nameWorkerList.map { data -> data.id })
        }
    }

    private fun initOnClick() {
        binding.apply {
            ibClose.setOnClickListener(onClickCallback)
            btnAdd.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ibClose -> {
                dismiss()
            }

            binding.btnAdd -> {
                viewModel.nameWorkerSavedState = NameWorkerSavedStateModel(nameWorkerList)
                viewModel.setNameWorker()
                dismiss()
                Timber.e(viewModel.nameWorkerSavedState.data.toString())

            }
        }
    }

}
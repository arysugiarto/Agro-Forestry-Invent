package com.agro.inventory.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.InventPlotEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.databinding.FragmentHomeBinding
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.util.*
import com.agro.inventory.viewmodel.AuthViewModel
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)
    private val viewModels by viewModels<LocalViewModel>()
    private val viewModelAuth by viewModels<AuthViewModel>()

    @Inject
    lateinit var accessManager: AccessManager


    private lateinit var area: List<InventPlotEntity>
    private var doubleBackToExitOnce: Boolean = emptyBoolean
    var userAccessId = emptyString

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        onBackPressed()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

    }

    private fun initOnClick() {
        binding.apply {
            tvTitle.setOnClickListener(onClickCallback)
            fab.setOnClickListener(onClickCallback)
            ivLogout.setOnClickListener(onClickCallback)
            btnInvent.setOnClickListener(onClickCallback)
            btnReInvent.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnInvent->{
                navController.navigateOrNull(
                    HomeFragmentDirections.actionHomeFragmentToInventAssigmentFragment(userAccessId)
                )
                Timber.e(userAccessId)
            }

            binding.btnReInvent->{
                navController.navigateOrNull(
                    HomeFragmentDirections.actionHomeFragmentToReInventAssigmentFragment()
                )
            }

            binding.ivLogout -> {
                val popupMenu = PopupMenu(requireContext(), view)

                popupMenu.menuInflater.inflate(R.menu.menu_logout, popupMenu.menu)

                popupMenu.show()

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.logout ->{
                            lifecycleScope.launch {
                                accessManager.setSession(
                                    session =  false
                                )
                            }

                            findNavController().navigateOrNull(
                                HomeFragmentDirections.actionLogout()
                            )
                        }

                    }
                    true
                }

            }

        }

    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitOnce) {
                activity?.finish()
            } else {
                doubleBackToExitOnce = true
                activity.toast(context?.getString(R.string.main_exit_app))
                Handler().postDelayed({
                    kotlin.run { doubleBackToExitOnce = false }
                }, 2000)
            }
        }
    }

}
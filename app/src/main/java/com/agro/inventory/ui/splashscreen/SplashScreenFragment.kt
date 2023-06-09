package com.agro.inventory.ui.splashscreen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.agro.inventory.ui.splashscreen.SplashScreenFragmentDirections
import com.agro.inventory.R
import com.agro.inventory.data.remote.Result
import com.agro.inventory.util.*
import com.agro.inventory.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashScreenFragment: Fragment(R.layout.fragment_splashscreen) {

    private val viewModel by hiltNavGraphViewModels<AuthViewModel>(R.id.auth)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        forceFullscreenStatusBar()

        viewModel.getSession()

        viewModel.session.observe(viewLifecycleOwner) {
           val session = it

            Timber.e(session.toString())

            if (session == true){
                navController.navigateOrNull(
                    SplashScreenFragmentDirections.actionMainFragment(),
                )

            }else{
                navController.navigateOrNull(
                    SplashScreenFragmentDirections.actionLoginFragment()
                )
            }

        }

        initLoginCallback()

    }

    private fun initLoginCallback() {
        viewModel.login.observe(viewLifecycleOwner) { result ->

            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    Timber.e("Login sukses")
                }
                is Result.Error -> {
                    requireView().snack(result.message)
                }
                else -> Unit
            }
        }
    }

    private fun forceFullscreenStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(
                WindowInsets.Type.statusBars()
            )
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun hideFullscreenStatusBar() {
        if (Build.VERSION.SDK_INT >= 30) {
            requireActivity().window.insetsController?.show(
                WindowInsets.Type.statusBars()
            )
        } else {
            requireActivity().window.clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        hideFullscreenStatusBar()
    }

}
package com.agro.inventory.ui.login

//import com.arysugiarto.attendence.ui.main.MainFragment.Companion.parentBottomAppBar
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.agro.inventory.ui.login.LoginFragmentDirections
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.AuthEntity
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.Result
import com.agro.inventory.data.remote.model.LoginRequest
import com.agro.inventory.databinding.FragmentLoginBinding
import com.agro.inventory.util.*
import com.agro.inventory.util.animatedtext.attachTextChangeAnimator
import com.agro.inventory.util.animatedtext.bindProgressButton
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.viewmodel.AuthViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding<FragmentLoginBinding>()
    private val viewModel by hiltNavGraphViewModels<AuthViewModel>(R.id.auth)
    private val viewModels by viewModels<LocalViewModel>()

    @Inject
    lateinit var accessManager: AccessManager

    var token = emptyString
    var sobiDate = emptyString
    private var authEntity: AuthEntity = AuthEntity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViewModelCallback()
        initOnClick()
        attachButtonAnimation()
        onInputTextChanged()

        viewModel.getTokenDataStore()
        viewModel.getSobiDataStore()
        viewModel.getSession()

        viewModel.tokenDatastore.observe(viewLifecycleOwner) {
            token = it.toString()
        }
        viewModel.sobiDateDatastore.observe(viewLifecycleOwner) {
            sobiDate = it.toString()
        }
    }

    private fun initViewModel() {

        viewModel.requestToken()

        var dataLogin = LoginRequest.Data(
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString().md5(),
            18
        )

        val data = LoginRequest(
            dataLogin
        )

        viewModel.requestLogin(
            "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
            "1550446421",
            data
        )

    }

    private fun initViewModelCallback() {
        initLoginCallback()
        initTokenCallback()
    }

    private fun initLoginCallback() {
        viewModel.login.observe(viewLifecycleOwner) { result ->

            result.attachLoadingButton(
                button = binding.btnLogin,
                endLoadingText = requireContext().getString(LOGIN_STRING_RES)
            ) {
                this.progressColor = Color.WHITE
            }

            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    context?.toast("Berhasil Login")
                    clearUserInput()
                    userSuccessLogin()
                    lifecycleScope.launch {
                        accessManager.setSession(
                           session =  true
                        )
//                        accessManager.setUserAccess(
//                            result.data?.data?.firstOrNull()?.userAccessId.toString()
//                        )
                    }
                    viewModels.deleteAuth()
                    authEntity = AuthEntity(
                        userAccessId = result.data?.data?.firstOrNull()?.userAccessId,
                        username =  result.data?.data?.firstOrNull()?.username,
                        firstname =  result.data?.data?.firstOrNull()?.firstname,
                        lastname =  result.data?.data?.firstOrNull()?.lastname,
                        roleTypeId =  result.data?.data?.firstOrNull()?.roleType,
                    )

                    viewModels.insertLocalAuth(authEntity)

                }
                is Error -> {
                    result.message?.let { requireView().snack(it) }
                    Timber.e("gagal masuk")
                }
                else -> Unit
            }
        }
    }

    private fun initTokenCallback() {
        viewModel.token.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {

                }
                is Result.Error<*> -> {}
                else -> {}
            }
        })
    }

    private fun userSuccessLogin() {
        navController.navigateOrNull(
            direction = LoginFragmentDirections.actionMainFragment(isLoggedIn = true),
            clearTask = true
        )
    }

    private fun attachButtonAnimation() {
        viewLifecycleOwner.bindProgressButton(binding.btnLogin)

        binding.btnLogin.attachTextChangeAnimator {
            textColorRes = COLOR_WHITE_RES
            useCurrentTextColor = !useCurrentTextColor
        }
    }

    private fun onInputTextChanged() {
        binding.boxUsername.editText?.addTextChangedListener {
            binding.boxUsername.error = null
        }
        binding.boxPassword.editText?.addTextChangedListener {
            binding.boxPassword.error = null
        }
    }

    private fun initOnClick() {
        binding.apply {
            btnLogin.setOnClickListener(onClickCallback)

        }
    }

    private fun clearUserInput() {
        binding.boxUsername.setText()
        binding.boxPassword.setText()
    }


    private val onClickCallback = View.OnClickListener { view ->
        when {
            binding.boxUsername.textIsEmpty() -> binding.boxUsername.warn(
                context?.getString(R.string.login_username_hint)
            )
            binding.boxPassword.textIsEmpty() -> binding.boxPassword.warn(
                context?.getString(R.string.login_password_hint)
            )
            else -> {
                initViewModel()
                activity.hideKeyboard(view)
            }


        }

    }

    companion object {
        const val COLOR_WHITE_RES = R.color.white
        const val LOGIN_STRING_RES = R.string.login_login
    }

}
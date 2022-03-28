package apps.plinqdevelopers.evercast.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.databinding.FragmentLoginBinding
import apps.plinqdevelopers.evercast.datastore.AppPreferences
import apps.plinqdevelopers.evercast.utils.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gotoAccountRegister()
        validateLoginEmail()
        validateAccountLogin()
    }

    private fun validateLoginEmail() {
        binding.apply {
            verifyEmailButton.setOnClickListener {
                if (TextUtils.isEmpty(email.text)) {
                    showErrorMessage(message = "Email field is marked as required!")
                } else {
                    verifyEmailButton.visibility = View.GONE
                    emailProgress.visibility = View.VISIBLE
                    val loginEmail: String = email.text.toString().trim()
                    val emailResponse = loginViewModel.getEmailAuthStatus(
                        payload = AuthenticationPayload(
                            authEmail = loginEmail,
                            authPassword = null,
                            authName = null
                        )
                    )
                    emailResponse.observe(requireActivity(), Observer { response ->
                        verifyEmailButton.isVisible = response is RequestManager.Error
                        emailProgress.isInvisible = response is RequestManager.Success

                        if (response.data != null) {
                            if (!response.data.success) {
                                verifyEmailButton.visibility = View.VISIBLE
                                showErrorMessage(message = "This email is not registered!")
                            } else {
                                loginPasswordContainer.visibility = View.VISIBLE
                                loginForgotContainer.visibility = View.VISIBLE
                                loginActionContainer.visibility = View.VISIBLE
                                loginRegister.visibility = View.GONE
                            }
                        }
                    })
                }
            }
        }
    }


    private fun validateAccountLogin() {
        binding.apply {
            loginButton.setOnClickListener {
                if (TextUtils.isEmpty(email.text) || TextUtils.isEmpty(password.text)) {
                    showErrorMessage(message = "All field is marked as required!")
                } else {
                    loginButton.visibility = View.GONE
                    loginProgress.visibility = View.VISIBLE
                    val loginEmail: String = email.text.toString().trim()
                    val loginPassword: String = password.text.toString().trim()

                    val emailResponse = loginViewModel.validateLogin(
                        payload = AuthenticationPayload(
                            authEmail = loginEmail,
                            authPassword = loginPassword,
                            authName = null
                        )
                    )
                    emailResponse.observe(requireActivity(), Observer { response ->
                        when (response) {
                            is RequestManager.Error -> {
                                loginProgress.visibility = View.GONE
                                loginButton.visibility = View.VISIBLE
                                showErrorMessage(message = "We could not verify your account. Please try again Later!")
                            }
                            is RequestManager.Success -> {
                                loginProgress.visibility = View.GONE

                                val account = response.data
                                if (account != null) {
                                    val preferences : AppPreferences = AppPreferences(
                                        accountID = account.accountID,
                                        accountName = account.username,
                                        accountEmail = account.email,
                                        accountCreated = account.created
                                    )
                                    lifecycleScope.launch {
                                        loginViewModel.savePreferences(appPreferences = preferences)
                                    }
                                }

                                gotoDashboard()
                            }
                            is RequestManager.Loading -> {}
                        }
                    })
                }
            }
        }
    }

    private fun gotoDashboard() {
        val gotoDashboard = LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
        findNavController().navigate(gotoDashboard)
    }


    private fun gotoAccountRegister() {
        binding.apply {
            loginRegister.setOnClickListener {
                val gotoRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(gotoRegister)
            }
        }
    }

    private fun showErrorMessage(message: String) {
        val snackBar: Snackbar = Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_INDEFINITE
        )

        snackBar.setAction("OK", View.OnClickListener {
            snackBar.dismiss()
        }).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
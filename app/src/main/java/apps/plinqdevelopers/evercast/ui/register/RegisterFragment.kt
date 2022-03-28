package apps.plinqdevelopers.evercast.ui.register

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.databinding.FragmentRegisterBinding
import apps.plinqdevelopers.evercast.datastore.AppPreferences
import apps.plinqdevelopers.evercast.utils.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerButton.setOnClickListener {
                if (TextUtils.isEmpty(username.text) || TextUtils.isEmpty(email.text) || TextUtils.isEmpty(
                        password.text
                    )
                ) {
                    showErrorMessage(message = "All fields are marked as required!")
                } else {
                    registerButton.visibility = View.GONE
                    registerProgress.visibility = View.VISIBLE
                    val regName: String = username.text.toString().trim()
                    val regEmail: String = email.text.toString().trim()
                    val regPassword: String = password.text.toString().trim()
                    val payload: AuthenticationPayload = AuthenticationPayload(
                        authName = regName,
                        authEmail = regEmail,
                        authPassword = regPassword
                    )
                    accountRegistration(payload = payload)
                }
            }
        }
    }

    private fun accountRegistration(payload: AuthenticationPayload) {
        val response = registerViewModel.accountRegistration(payload = payload)
        binding.apply {
            response.observe(requireActivity(), Observer { account ->
                when (account) {
                    is RequestManager.Loading -> {

                    }
                    is RequestManager.Error -> {
                        registerProgress.visibility = View.GONE
                        registerButton.visibility = View.VISIBLE
                        showErrorMessage(message = "We could not authenticate your account. Please try again later!")
                    }
                    is RequestManager.Success -> {
                        registerProgress.visibility = View.GONE
                        val accountData: DomainAccount? = account.data
                        if (accountData != null) {
                            lifecycleScope.launch {
                                registerViewModel.savePreferences(
                                    appPreferences = AppPreferences(
                                        accountID = accountData.accountID,
                                        accountName = accountData.username,
                                        accountEmail = accountData.email,
                                        accountCreated = accountData.created
                                    )
                                )
                            }
                        }
                        gotoDashboard()
                    }
                }
            })
        }
    }

    private fun gotoDashboard() {
        val gotoDash = RegisterFragmentDirections.actionRegisterFragmentToDashboardFragment()
        findNavController().navigate(gotoDash)
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
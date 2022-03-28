package apps.plinqdevelopers.evercast.ui.splash

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.databinding.FragmentSplashBinding
import apps.plinqdevelopers.evercast.datastore.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel : SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                lifecycleScope.launch {
                    splashViewModel.appPreferences.collect { preferences ->
                        val accountID : String = preferences.accountID
                        if (accountID.isEmpty()) {
                            val gotoLogin = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                            findNavController().navigate(gotoLogin)
                        } else {
                            val gotoDash = SplashFragmentDirections.actionSplashFragmentToDashboardFragment()
                            findNavController().navigate(gotoDash)
                        }
                    }
                }
            }
        }

        loadTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
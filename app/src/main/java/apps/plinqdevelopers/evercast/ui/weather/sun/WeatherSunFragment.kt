package apps.plinqdevelopers.evercast.ui.weather.sun

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import apps.plinqdevelopers.evercast.databinding.FragmentWeatherSunBinding
import apps.plinqdevelopers.evercast.ui.weather.adapters.WeatherForecastAdapter
import apps.plinqdevelopers.evercast.utils.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherSunFragment : Fragment() {
    private var _binding: FragmentWeatherSunBinding? = null
    private val binding get() = _binding!!

    private val sunViewModel: SunViewModel by viewModels()
    private val forecastAdapter: WeatherForecastAdapter = WeatherForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherSunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherList.apply {
            hasFixedSize()
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val arguments = navArgs<WeatherSunFragmentArgs>()
        val weather: DomainWeather = arguments.value.weatherArgs

        val locationLong: String = arguments.value.locationLong
        val locationLat: String = arguments.value.locationLat
        val weatherID = weather.Id

        if (weatherID != null ) {
            loadWeatherForecast(lat = locationLat, long = locationLong, weatherID = weatherID)
            loadWeatherInformation(weather = weather)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadWeatherInformation(weather: DomainWeather) {
        binding.apply {
            mainWeatherValue.text = weather.mainTemp!!.toString() + "\u00B0"
            minValue.text = weather.mainTempMin!!.toString() + "\u00B0"
            currentValue.text = weather.mainTemp!!.toString() + "\u00B0"
            maxValue.text = weather.mainTempMax!!.toString() + "\u00B0"
        }
    }

    private fun loadWeatherForecast(lat: String, long: String, weatherID: String) {
        binding.apply {
            lifecycleScope.launch {
                sunViewModel.appPreferences.collect { preference ->
                    val accountID: String = preference.accountID

                    val response = sunViewModel.loadForecast(
                        payload = WeatherPayload(
                            lat = lat,
                            long = long,
                            accountID = accountID,
                            weatherID = weatherID
                        )
                    )
                    response.observe(requireActivity(), Observer { forecast ->
                        when (forecast) {
                            is RequestManager.Loading -> {

                            }
                            is RequestManager.Error -> {
                                showErrorMessage(message = "Error: " + forecast.error)
                            }
                            is RequestManager.Success -> {
                                forecastAdapter.submitList(forecast.data)
                            }
                        }
                    })
                }
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
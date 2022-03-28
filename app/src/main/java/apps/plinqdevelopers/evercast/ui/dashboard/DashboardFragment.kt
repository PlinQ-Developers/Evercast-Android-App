package apps.plinqdevelopers.evercast.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import com.mapbox.geojson.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import apps.plinqdevelopers.evercast.databinding.FragmentDashboardBinding
import apps.plinqdevelopers.evercast.utils.RequestManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-1.391401, 36.764743)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    private var savedWeather: DomainWeather? = null

    private var selectedLocation: LatLng? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
        getDeviceLocation()
        //setupWeatherNavigation()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val appLong: Double = lastKnownLocation!!.longitude
                            val appLat: Double = lastKnownLocation!!.latitude

                            selectedLocation = LatLng(appLat, appLong)
                            binding.mapsContainer.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
                            val initialCameraOptions = CameraOptions.Builder()
                                .center(Point.fromLngLat(appLong, appLat))
                                .zoom(15.5)
                                .bearing(-17.6)
                                .build()

                            binding.mapsContainer.apply {
                                getMapboxMap().setCamera(initialCameraOptions)
                                lifecycleScope.launch {
                                    dashboardViewModel.appPreferences.collect { preferences ->
                                        val accountID : String = preferences.accountID

                                        val payload: WeatherPayload = WeatherPayload(
                                            lat = appLat.toString(),
                                            long = appLong.toString(),
                                            accountID = accountID,
                                            weatherID = ""
                                        )
                                        loadWeatherInformation(payload = payload)
                                    }
                                }

                            }

                        }
                    } else {
                        showErrorMessage(message = "We could not get your location. Using default location!")
                        selectedLocation = defaultLocation
                        val initialCameraOptions = CameraOptions.Builder()
                            .center(
                                Point.fromLngLat(
                                    defaultLocation.longitude,
                                    defaultLocation.latitude
                                )
                            )
                            .zoom(15.5)
                            .bearing(-17.6)
                            .build()

                        binding.mapsContainer.apply {
                            getMapboxMap().setCamera(initialCameraOptions)
                            lifecycleScope.launch {
                                dashboardViewModel.appPreferences.collect { preferences ->
                                    val accountID : String = preferences.accountID

                                    val payload: WeatherPayload = WeatherPayload(
                                        lat = defaultLocation.latitude.toString(),
                                        long = defaultLocation.longitude.toString(),
                                        accountID = accountID,
                                        weatherID = ""
                                    )
                                    loadWeatherInformation(payload = payload)
                                }
                            }
                        }
                    }
                }
                binding.getWeather.setOnClickListener {
                    val gotoList =
                        DashboardFragmentDirections.actionDashboardFragmentToFavoriteListFragment()
                    findNavController().navigate(gotoList)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadWeatherInformation(payload: WeatherPayload) {
        val weatherResponse = dashboardViewModel.loadWeather(payload = payload)
        weatherResponse.observe(requireActivity(), Observer { weatherList ->
            when (weatherList) {
                is RequestManager.Loading -> {}
                is RequestManager.Error -> {
                    showErrorMessage(message = "Error: ${weatherList.error}")
                }
                is RequestManager.Success<List<DomainWeather>> -> {
                    if (weatherList.data != null) {
                        val myWeatherList: List<DomainWeather>? = weatherList.data
                        if (myWeatherList != null) {
                            val latestWeather: DomainWeather = myWeatherList[0]
                            binding.apply {
                                savedWeather = latestWeather
                                moreDetailsButton.visibility = View.VISIBLE
                                weatherValue.text = latestWeather.mainTemp+ "\u00B0"
                                val name: String =
                                    "${latestWeather.locationName} - ${latestWeather.weatherMain}"
                                weatherCountryName.text = name
                                weatherMinValue.text =
                                    latestWeather.mainTempMin + "\u00B0"
                                weatherCurrentValue.text =
                                    latestWeather.mainTemp+ "\u00B0"
                                weatherMaxValue.text =
                                    latestWeather.mainTempMax+ "\u00B0"
                                setupWeatherNavigation()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setupWeatherNavigation() {
        binding.moreDetailsButton.setOnClickListener {
            if (savedWeather != null && selectedLocation != null) {
                val gotoRain =
                    DashboardFragmentDirections.actionDashboardFragmentToWeatherRainFragment(
                        weatherArgs = savedWeather!!,
                        locationLat = selectedLocation!!.latitude.toString(),
                        locationLong = selectedLocation!!.longitude.toString()
                    )
                val gotoCloud =
                    DashboardFragmentDirections.actionDashboardFragmentToWeatherCloudFragment(
                        weatherArgs = savedWeather!!,
                        locationLat = selectedLocation!!.latitude.toString(),
                        locationLong = selectedLocation!!.longitude.toString()
                    )
                val gotoSun =
                    DashboardFragmentDirections.actionDashboardFragmentToWeatherSunFragment(
                        weatherArgs = savedWeather!!,
                        locationLat = selectedLocation!!.latitude.toString(),
                        locationLong = selectedLocation!!.longitude.toString()
                    )

                when (savedWeather?.weatherMain.toString()) {
                    "Rain" -> {
                        findNavController().navigate(gotoRain)
                    }
                    "Clouds" -> {
                        findNavController().navigate(gotoCloud)
                    }
                    "Sun" -> {
                        findNavController().navigate(gotoSun)
                    }
                }
            } else {
                showErrorMessage(message = "An error occurred!")
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
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

        snackBar.setAction("OK") {
            snackBar.dismiss()
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
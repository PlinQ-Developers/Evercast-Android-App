package apps.plinqdevelopers.evercast.ui.weather.rain

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import apps.plinqdevelopers.evercast.data.domain.DomainWeatherList
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import apps.plinqdevelopers.evercast.datastore.PreferencesManager
import apps.plinqdevelopers.evercast.repositories.WeatherRepository
import apps.plinqdevelopers.evercast.utils.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RainViewModel
@Inject
constructor(
    private val repository: WeatherRepository,
    private val preferences : PreferencesManager

) : ViewModel(), LifecycleObserver {

    fun loadForecast(payload: WeatherPayload) : LiveData<RequestManager<List<DomainWeatherList>>> {
        return repository.loadForecast(payload = payload).asLiveData()
    }

    val appPreferences = preferences.preferencesFlow

}
package apps.plinqdevelopers.evercast.ui.favorites.list

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import apps.plinqdevelopers.evercast.repositories.WeatherRepository
import apps.plinqdevelopers.evercast.utils.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val repository: WeatherRepository
) : ViewModel(), LifecycleObserver {

    fun savedWeather(): LiveData<List<DomainWeather>> = repository.loadSavedWeather().asLiveData()
}
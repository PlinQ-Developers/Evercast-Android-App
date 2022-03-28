package apps.plinqdevelopers.evercast.ui.register

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.datastore.AppPreferences
import apps.plinqdevelopers.evercast.datastore.PreferencesManager
import apps.plinqdevelopers.evercast.repositories.AuthenticationRepository
import apps.plinqdevelopers.evercast.utils.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(
    private val repository: AuthenticationRepository,
    private val preferences: PreferencesManager
) : ViewModel(), LifecycleObserver {

    fun accountRegistration(payload: AuthenticationPayload): LiveData<RequestManager<DomainAccount>> {
        return repository.accountRegister(payload = payload).asLiveData()
    }

    suspend fun savePreferences(appPreferences: AppPreferences) {
        preferences.updateAuthToken(
            appPreferences = appPreferences
        )
    }
}
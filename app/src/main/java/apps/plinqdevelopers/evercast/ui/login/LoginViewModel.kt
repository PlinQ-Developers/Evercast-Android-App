package apps.plinqdevelopers.evercast.ui.login

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.network.NetworkAccountVerification
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.datastore.AppPreferences
import apps.plinqdevelopers.evercast.datastore.PreferencesManager
import apps.plinqdevelopers.evercast.repositories.AuthenticationRepository
import apps.plinqdevelopers.evercast.utils.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val repository: AuthenticationRepository,
    private val preferences: PreferencesManager
) : ViewModel(), LifecycleObserver {

    fun getEmailAuthStatus(payload: AuthenticationPayload) : LiveData<RequestManager<NetworkAccountVerification>> {
        return repository.getEmailStatus(payload = payload).asLiveData()
    }

    fun validateLogin(payload: AuthenticationPayload) : LiveData<RequestManager<DomainAccount>> {
        return repository.accountLogin(payload = payload).asLiveData()
    }

    suspend fun savePreferences(appPreferences: AppPreferences) {
        preferences.updateAuthToken(
            appPreferences = appPreferences
        )
    }

}
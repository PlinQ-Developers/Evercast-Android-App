package apps.plinqdevelopers.evercast.ui.splash

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import apps.plinqdevelopers.evercast.datastore.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val preferences: PreferencesManager
) : ViewModel(), LifecycleObserver {

    val appPreferences = preferences.preferencesFlow

}
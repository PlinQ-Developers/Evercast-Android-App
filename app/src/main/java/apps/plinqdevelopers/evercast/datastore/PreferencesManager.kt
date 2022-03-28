package apps.plinqdevelopers.evercast.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class PreferencesManager
@Inject
constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        const val PREFERENCE_NAME = "apps.plinqdevelopers.evercast.settings"
        val APP_ACCOUNT_ID = stringPreferencesKey("account_id")
        val APP_ACCOUNT_NAME = stringPreferencesKey("account_name")
        val APP_ACCOUNT_EMAIL = stringPreferencesKey("account_email")
        val APP_ACCOUNT_CREATED = stringPreferencesKey("account_created")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            mapPreferences(preferences = preferences)
        }

    suspend fun updateAuthToken(appPreferences: AppPreferences) {
        dataStore.edit { preferences ->
            preferences[APP_ACCOUNT_ID] = appPreferences.accountID
            preferences[APP_ACCOUNT_NAME] = appPreferences.accountName
            preferences[APP_ACCOUNT_EMAIL] = appPreferences.accountEmail
            preferences[APP_ACCOUNT_CREATED] = appPreferences.accountCreated
        }
    }

    private fun mapPreferences(preferences: Preferences) : AppPreferences {
        val accountID = preferences[APP_ACCOUNT_ID] ?: ""
        val authName = preferences[APP_ACCOUNT_NAME] ?: ""
        val authEmail = preferences[APP_ACCOUNT_EMAIL] ?: ""
        val authCreated = preferences[APP_ACCOUNT_CREATED] ?: ""

        return AppPreferences(
            accountID = accountID,
            accountEmail = authEmail,
            accountName = authName,
            accountCreated = authCreated
        )
    }

}

data class AppPreferences(
    val accountID : String,
    val accountName : String,
    val accountEmail: String,
    val accountCreated : String
)
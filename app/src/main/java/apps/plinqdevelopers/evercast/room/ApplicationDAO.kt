package apps.plinqdevelopers.evercast.room

import androidx.room.*
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.domain.DomainWeatherList
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDAO {


    //Accounts Manager
    @Query("SELECT * FROM accountTable")
    fun loadAccountDetails(): Flow<DomainAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountDetails(account: DomainAccount)

    @Query("DELETE FROM accountTable")
    suspend fun deleteAccountDetails()

    @Transaction
    suspend fun manageAccountDetails(account: DomainAccount) {
        deleteAccountDetails()
        insertAccountDetails(account = account)
    }


    //Current Weather Manager
    @Query("SELECT * FROM weatherTable")
    fun loadWeather(): Flow<List<DomainWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: List<DomainWeather>)

    @Query("DELETE FROM weatherTable")
    suspend fun deleteWeather()

    @Transaction
    suspend fun manageWeather(weather: List<DomainWeather>) {
        //deleteWeather()
        insertWeather(weather = weather)
    }


    //Forecast Weather Manager
    @Query("SELECT * FROM weatherListTable")
    fun loadForecast(): Flow<List<DomainWeatherList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecasts: List<DomainWeatherList>)

    @Query("DELETE FROM weatherListTable")
    suspend fun deleteForecast()

    @Transaction
    suspend fun manageForecasting(forecasts: List<DomainWeatherList>) {
        //deleteForecast()
        insertForecast(forecasts = forecasts)
    }

}
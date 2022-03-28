package apps.plinqdevelopers.evercast.api


import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.domain.DomainWeatherList
import apps.plinqdevelopers.evercast.data.network.NetworkAccount
import apps.plinqdevelopers.evercast.data.network.NetworkAccountVerification
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApplicationAPI {
    companion object {
        const val APP_BASE_URL = "https://evercast.herokuapp.com/app/"
    }

    @POST("verify/email")
    suspend fun verifyAuthPayload(
        @Body authPayload: AuthenticationPayload
    ) : NetworkAccountVerification


    @POST("login")
    suspend fun accountLogin(
        @Body authPayload : AuthenticationPayload
    ) : NetworkAccount


    @POST("register")
    suspend fun accountRegister(
        @Body authPayload: AuthenticationPayload
    ) : NetworkAccount


    @POST("weather/current")
    suspend fun getCurrentWeather(
        @Body weatherPayload: WeatherPayload
    ) : List<DomainWeather>


    @POST("weather/forecast")
    suspend fun getWeatherForecast(
        @Body weatherPayload: WeatherPayload
    ) : List<DomainWeatherList>

}
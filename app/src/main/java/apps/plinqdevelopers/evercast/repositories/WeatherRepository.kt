package apps.plinqdevelopers.evercast.repositories

import apps.plinqdevelopers.evercast.api.ApplicationAPI
import apps.plinqdevelopers.evercast.data.payload.WeatherPayload
import apps.plinqdevelopers.evercast.room.ApplicationDAO
import apps.plinqdevelopers.evercast.room.ApplicationDatabase
import apps.plinqdevelopers.evercast.utils.networkBoundResource
import javax.inject.Inject


class WeatherRepository
@Inject
constructor(
    private val applicationAPI: ApplicationAPI,
    applicationDatabase: ApplicationDatabase
) {
    private val applicationDAO : ApplicationDAO = applicationDatabase.applicationDAO()

    fun loadWeather(payload: WeatherPayload) = networkBoundResource(
        query = {
            applicationDAO.loadWeather()
        },
        fetch = {
            applicationAPI.getCurrentWeather(weatherPayload = payload)
        },
        saveFetchedResults = { weather ->
            applicationDAO.manageWeather(weather = weather)
        }
    )

    fun loadForecast(payload: WeatherPayload) = networkBoundResource(
        query = {
            applicationDAO.loadForecast()
        },
        fetch = {
            applicationAPI.getWeatherForecast(weatherPayload = payload)
        },
        saveFetchedResults = { forecast ->
            applicationDAO.manageForecasting(forecasts = forecast)
        }
    )

    fun loadSavedWeather() = applicationDAO.loadWeather()
}
package apps.plinqdevelopers.evercast.room

import androidx.room.Database
import androidx.room.RoomDatabase
import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.data.domain.DomainWeatherList

@Database(
    entities = [
        DomainAccount::class,
        DomainWeather::class,
        DomainWeatherList::class
    ],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun applicationDAO(): ApplicationDAO
}
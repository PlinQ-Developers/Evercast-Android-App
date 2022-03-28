package apps.plinqdevelopers.evercast.data.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "weatherTable")
data class DomainWeather(

    @PrimaryKey(autoGenerate = true)
    var weatherID : Int = 0,

    @SerializedName("_id")
    var Id: String? = null,

    @SerializedName("accountID")
    var accountID: String? = null,

    @SerializedName("lat")
    var lat: String? = null,

    @SerializedName("long")
    var long: String? = null,

    @SerializedName("locationID")
    var locationID: String? = null,

    @SerializedName("locationName")
    var locationName: String? = null,

    @SerializedName("locationTimezone")
    var locationTimezone: String? = null,

    @SerializedName("mainTemp")
    var mainTemp: String? = null,

    @SerializedName("mainTempMin")
    var mainTempMin: String? = null,

    @SerializedName("mainTempMax")
    var mainTempMax: String? = null,

    @SerializedName("mainTempFeelsLike")
    var mainTempFeelsLike: String? = null,

    @SerializedName("weatherMain")
    var weatherMain: String? = null,

    @SerializedName("__v")
    var version: Int? = null

) : Parcelable
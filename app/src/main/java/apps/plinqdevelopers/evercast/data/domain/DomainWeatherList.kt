package apps.plinqdevelopers.evercast.data.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "weatherListTable")
data class DomainWeatherList(

    @PrimaryKey(autoGenerate = true)
    val weatherListID : Int = 0,

    @SerializedName("_id")
    var listID: String? = null,

    @SerializedName("weatherID")
    var weatherID: String? = null,

    @SerializedName("dt")
    var dt: String? = null,

    @SerializedName("weatherName")
    var weatherName: String? = null,

    @SerializedName("weatherValue")
    var weatherValue: String? = null,

    @SerializedName("__v")
    var version: Int? = null

) : Parcelable {


}

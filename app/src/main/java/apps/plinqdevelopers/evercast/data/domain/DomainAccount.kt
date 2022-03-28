package apps.plinqdevelopers.evercast.data.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "accountTable")
@Parcelize
data class DomainAccount(

    @PrimaryKey
    var accountID : String,

    var username : String,

    var email : String,

    var password : String,

    var created : String

) : Parcelable

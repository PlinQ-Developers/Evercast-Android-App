package apps.plinqdevelopers.evercast.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkAccount(

    @Expose
    @SerializedName("_id")
    val accountID : String,

    @Expose
    @SerializedName("accountName")
    val username : String,

    @Expose
    @SerializedName("accountEmail")
    val email : String,

    @Expose
    @SerializedName("accountPassword")
    val password : String,

    @Expose
    @SerializedName("accountCreated")
    val created : String
)

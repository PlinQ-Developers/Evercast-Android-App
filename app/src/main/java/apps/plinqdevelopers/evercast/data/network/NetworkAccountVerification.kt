package apps.plinqdevelopers.evercast.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkAccountVerification(

    @Expose
    @SerializedName("success")
    val success : Boolean,

    @Expose
    @SerializedName("message")
    val message : String

)

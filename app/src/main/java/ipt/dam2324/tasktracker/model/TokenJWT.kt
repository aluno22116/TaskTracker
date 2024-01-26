package ipt.dam2324.tasktracker.model

import com.google.gson.annotations.SerializedName

data class TokenJWT(
    @SerializedName("token") val token: String?
)

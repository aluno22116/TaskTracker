package ipt.dam2324.tasktracker.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int?,
    @SerializedName("nome") val nome: String?,
    @SerializedName("apelido") val apelido: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)

data class UserResponse(
    @SerializedName("users") val users: List<User>?,
)
data class UserRequest(
    @SerializedName("user") val user: User?,
)

package ipt.dam2324.tasktracker.model

import com.google.gson.annotations.SerializedName

// Representação de um utilizador
data class User(
    @SerializedName("id") val id: Int?,
    @SerializedName("nome") val nome: String?,
    @SerializedName("apelido") val apelido: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)

// Representação da resposta da API contendo uma lista de utilizadores
data class UserResponse(
    @SerializedName("users") val users: List<User>?,
)
// Representação de uma solicitação de utilizador para a API
data class UserRequest(
    @SerializedName("user") val user: User?,
)

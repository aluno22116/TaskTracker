package ipt.dam2324.tasktracker.model

import com.google.gson.annotations.SerializedName

// Representação de uma nota
data class Note(
    @SerializedName("id") val id: Int?,
    @SerializedName("username") val username: String?,
    @SerializedName("notas") val notas: String?,
    @SerializedName("imagem") val imagem: String?,
)

// Representação da resposta da API contendo uma lista de notas
data class NoteResponse(
    @SerializedName("notes") val notes: List<Note>?,
)
// Representação de uma solicitação de nota para a API
data class NoteRequest(
    @SerializedName("note") val note: Note?,
)
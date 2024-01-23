package com.example.trabalhofinal.model

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("id") val id: Int?,
    @SerializedName("username") val username: String?,
    @SerializedName("notas") val notas: String?,
    @SerializedName("imagem") val imagem: String?,
)

data class NoteResponse(
    @SerializedName("notes") val notes: List<Note>?,
)
data class NoteRequest(
    @SerializedName("note") val note: Note?,
)
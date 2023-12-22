package com.example.trabalhofinal.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("descricao") val descricao: String?,
    @SerializedName("dataFim") val dataFim: String?,
    @SerializedName("estado") val estado: Boolean?
)

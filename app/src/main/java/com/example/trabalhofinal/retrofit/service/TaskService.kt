package com.example.trabalhofinal.retrofit.service

import com.example.trabalhofinal.model.Task
import retrofit2.Call
import retrofit2.http.GET

interface TaskService {
    @GET("tasks")
    fun getTasks(): Call<List<Task>>
}
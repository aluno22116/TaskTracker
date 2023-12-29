package com.example.trabalhofinal.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.trabalhofinal.retrofit.service.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val host = "https://api.sheety.co/fbdf7989b3d662d677c247d1ef56dc15/dam/"



    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun service() = retrofit.create(Service::class.java)
}
package com.example.trabalhofinal.retrofit

import com.example.trabalhofinal.retrofit.service.Service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val host = "https://api.sheety.co/b0ca613db63c618c47afa37626571590/dam/"



    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun service() = retrofit.create(Service::class.java)
}
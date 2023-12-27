package com.example.trabalhofinal.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.trabalhofinal.retrofit.service.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val host = "https://api.sheety.co/e6a5a728669f1feee0c9e5d8dc39020e/dam/"



    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun service() = retrofit.create(Service::class.java)
}
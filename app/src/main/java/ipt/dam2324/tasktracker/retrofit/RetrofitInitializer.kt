package ipt.dam2324.tasktracker.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ipt.dam2324.tasktracker.retrofit.service.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val host = "https://api.sheety.co/52f802ee17c96ec14a5f896e678e8301/dam/"



    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun service() = retrofit.create(Service::class.java)
}
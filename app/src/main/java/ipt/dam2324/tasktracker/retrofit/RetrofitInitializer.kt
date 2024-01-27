package ipt.dam2324.tasktracker.retrofit

import ipt.dam2324.tasktracker.retrofit.service.Service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val host = "https://api.sheety.co/f369384aac47b2fc50516a9984538a98/dam/"



    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun service() = retrofit.create(Service::class.java)
}
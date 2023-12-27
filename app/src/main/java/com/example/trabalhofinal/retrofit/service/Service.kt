package com.example.trabalhofinal.retrofit.service

import com.example.trabalhofinal.model.TokenJWT
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Service {
    @GET("users")
    fun getUsers(@Header("Authorization") token: String): Call<UserResponse>


    @FormUrlEncoded
    @POST("users")
    fun loginJWT(@Field("username") username: String?,
                 @Field("password") password: String?): Call<TokenJWT>

    @POST("users")
    fun addUser(@Body user: UserRequest): Call<UserRequest>

 //   @POST("users")
 //   fun addUser(@Header("Authorization") token: String, @Body user: UserRequest): Call<UserRequest>

}
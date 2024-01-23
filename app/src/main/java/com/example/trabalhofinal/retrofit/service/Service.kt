package com.example.trabalhofinal.retrofit.service

import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.model.NoteResponse
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
import retrofit2.http.PUT
import retrofit2.http.Path

interface Service {
    @GET("users")
    fun getUsers(@Header("Authorization") token: String): Call<UserResponse>

    @GET("notes")
    fun getNotes(@Header("Authorization") token: String): Call<NoteResponse>



    @FormUrlEncoded
    @POST("users")
    fun loginJWT(@Field("username") username: String?,
                 @Field("password") password: String?): Call<TokenJWT>



    @POST("users")
    fun addUser(@Header("Authorization") token: String, @Body user: UserRequest): Call<UserRequest>

    @POST("notes")
    fun createNoteSpace(@Header("Authorization") token: String, @Body note: NoteRequest): Call<NoteRequest>

    @PUT("notes/{userId}")
    fun updateNote(@Header("Authorization") token: String, @Path("userId") userId: String?, @Body updatedNote: NoteRequest): Call<NoteRequest>









   @POST("notes/{userId}")
   fun addUserNotes(@Header("Authorization") token: String, @Path("userId") userId: String, @Body notes: List<NoteRequest>): Call<List<NoteRequest>>




        @PUT("notes/{userId}")
        fun addOrUpdateNotes(@Header("Authorization") token: String, @Path("userId") userId: String, @Body notes: List<NoteRequest>): Call<List<NoteRequest>>



  //  @GET("notes/{userId}")
  //  fun getNotesBid(@Header("Authorization") token: String, @Path("userId") userId: String?, @Body getNotesBid: List<NoteRequest>): Call<List<NoteRequest>>


  // @GET("notes/{userId}")
  // fun getNotesBid(@Header("Authorization") token: String, @Path("userId") userId: String?): Call<List<NoteRequest>>

}
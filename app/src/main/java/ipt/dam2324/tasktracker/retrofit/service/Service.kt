package ipt.dam2324.tasktracker.retrofit.service

import ipt.dam2324.tasktracker.model.NoteRequest
import ipt.dam2324.tasktracker.model.NoteResponse
import ipt.dam2324.tasktracker.model.TokenJWT
import ipt.dam2324.tasktracker.model.UserRequest
import ipt.dam2324.tasktracker.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("users")
    fun getUsers(@Header("Authorization") token: String): Call<UserResponse>

 //   @GET("notes")
 //   fun getNotes(@Header("Authorization") token: String, userId: String): Call<NoteResponse>

    @GET("notes")
    fun getNotes(@Header("Authorization") authorization: String, @Query("userId") userId: String): Call<NoteResponse>



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

//   @Multipart
//   @PUT("notes/{userId}")
//   fun updateImg(
//       @Header("Authorization") authorization: String,
//       @Path("userId") userId: String,
//       @Part("note") note: NoteRequest,
//       @Part image: MultipartBody.Part
//   ): Call<NoteRequest>

//    @Multipart
//    @PUT("notes/{userId}")
//    fun updateImg(
//        @Header("Authorization") authorization: String,
//        @Path("userId") userId: String,
//        @Part("note") note: RequestBody,
//        @Part("outroCampo") outroCampo: RequestBody,
//        @Part image: MultipartBody.Part
//    ): Call<NoteRequest>









    @POST("notes/{userId}")
   fun addUserNotes(@Header("Authorization") token: String, @Path("userId") userId: String, @Body notes: List<NoteRequest>): Call<List<NoteRequest>>




        @PUT("notes/{userId}")
        fun addOrUpdateNotes(@Header("Authorization") token: String, @Path("userId") userId: String, @Body notes: List<NoteRequest>): Call<List<NoteRequest>>



  //  @GET("notes/{userId}")
  //  fun getNotesBid(@Header("Authorization") token: String, @Path("userId") userId: String?, @Body getNotesBid: List<NoteRequest>): Call<List<NoteRequest>>


  // @GET("notes/{userId}")
  // fun getNotesBid(@Header("Authorization") token: String, @Path("userId") userId: String?): Call<List<NoteRequest>>

}
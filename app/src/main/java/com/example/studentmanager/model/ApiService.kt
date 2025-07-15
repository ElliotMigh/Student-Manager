package com.example.studentmanager

import android.telecom.Call
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ApiService {

    @GET("/student")
    fun getAllStudent(): Single<List<Student>>

    @POST("/student")
    fun insertStudent(@Body body: JsonObject): retrofit2.Call<List<String>>

    @PUT("/student/updating{name}")
    fun updateStudent(
        @Path("name") name: String,
        @Body body: JsonObject
    ): retrofit2.Call<List<String>>

    @DELETE("/student/deleting{name}")
    fun deleteStudent(@Path("name") name:String) : retrofit2.Call<String>



}
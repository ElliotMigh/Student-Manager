package com.example.studentmanager.model

import com.example.studentmanager.model.api.ApiService
import com.example.studentmanager.model.local.student.Student
import com.example.studentmanager.util.BASE_URL
import com.example.studentmanager.util.studentToJsonObject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getAllStudent(): Single<List<Student>> {
        return apiService.getAllStudent()
    }

    fun addStudent(student: Student): Completable {
        return apiService.insertStudent(studentToJsonObject(student)).ignoreElement()
    }

    fun updateStudent(student: Student): Completable {
        return apiService.updateStudent(student.name, studentToJsonObject(student)).ignoreElement()
    }

    fun removeStudent(studentName: String): Completable {
        return apiService.deleteStudent(studentName).ignoreElement()
    }
}
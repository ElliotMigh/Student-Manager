package com.example.studentmanager.model

import androidx.lifecycle.LiveData
import com.example.studentmanager.model.api.ApiService
import com.example.studentmanager.model.local.student.StudentDao
import com.example.studentmanager.model.local.student.Student
import com.example.studentmanager.util.studentToJsonObject
import io.reactivex.rxjava3.core.Completable

class MainRepository(
    private val apiService: ApiService,
    private val studentDao: StudentDao
) {

    fun getAllStudent(): LiveData<List<Student>> {
        return studentDao.getAllData()
    }

    //caching
    fun refreshData(): Completable {
        return apiService.getAllStudent()
            .doOnSuccess {
                studentDao.insertAll(it)
            }.ignoreElement()
    }

    fun insertStudent(student: Student): Completable {
        return apiService
            .insertStudent(studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun updateStudent(student: Student): Completable {
        return apiService.updateStudent(student.name, studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun removeStudent(studentName: String): Completable {
        return apiService.deleteStudent(studentName)
            .doOnComplete {
                studentDao.remove(studentName)
            }
    }


//    fun getAllStudent(): Single<List<Student>> {
//        return apiService.getAllStudent()
//    }
//
//    fun addStudent(student: Student): Completable {
//        return apiService.insertStudent(studentToJsonObject(student)).ignoreElement()
//    }
//
//    fun updateStudent(student: Student): Completable {
//        return apiService.updateStudent(student.name, studentToJsonObject(student)).ignoreElement()
//    }
//
//    fun removeStudent(studentName: String): Completable {
//        return apiService.deleteStudent(studentName).ignoreElement()
//    }
}
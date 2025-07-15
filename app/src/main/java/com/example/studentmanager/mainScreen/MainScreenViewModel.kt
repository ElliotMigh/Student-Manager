package com.example.studentmanager.mainScreen

import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.Student
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainScreenViewModel(private val mainRepository: MainRepository) {

    val progressBarSubject = BehaviorSubject.create<Boolean>()

    fun getAllStudent(): Single<List<Student>> {

        progressBarSubject.onNext(true)

        return mainRepository
            .getAllStudent()
            .doFinally {
                progressBarSubject.onNext(false)
            }

    }

    fun removeStudent(studentName: String): Completable {
        return mainRepository.removeStudent(studentName)
    }
}
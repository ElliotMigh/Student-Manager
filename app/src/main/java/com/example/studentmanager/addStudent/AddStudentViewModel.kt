package com.example.studentmanager.addStudent

import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.Student
import io.reactivex.rxjava3.core.Completable

class AddStudentViewModel(private val mainRepository: MainRepository) {

    fun insertNewStudent(student: Student): Completable {
        return mainRepository.addStudent(student)
    }

    fun updateNewStudent(student: Student): Completable {
        return mainRepository.updateStudent(student)
    }

}
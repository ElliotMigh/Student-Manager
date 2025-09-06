package com.example.studentmanager.addStudent

import androidx.lifecycle.ViewModel
import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.local.student.Student
import io.reactivex.rxjava3.core.Completable

class AddStudentViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun insertNewStudent(student: Student): Completable {
        return mainRepository.insertStudent(student)
    }

    fun updateNewStudent(student: Student): Completable {
        return mainRepository.updateStudent(student)
    }

}
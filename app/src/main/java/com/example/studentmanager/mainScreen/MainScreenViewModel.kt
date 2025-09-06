package com.example.studentmanager.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.local.student.Student
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainScreenViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private lateinit var netDisposable: Disposable
    private val errorData = MutableLiveData<String>()

    init {
        mainRepository.refreshData()
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    netDisposable = d
                }

                override fun onComplete() {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Throwable) {
                    errorData.postValue(e.message ?: "unkhown error!")
                }

            })
    }

//    val progressBarSubject = BehaviorSubject.create<Boolean>()
//
//    fun getAllStudent(): Single<List<Student>> {
//
//        progressBarSubject.onNext(true)
//
//        return mainRepository
//            .getAllStudent()
//            .doFinally {
//                progressBarSubject.onNext(false)
//            }
//
//    }

    fun getAllData() : LiveData<List<Student>> {
        return mainRepository.getAllStudent()
    }

    fun removeStudent(studentName: String): Completable {
        return mainRepository.removeStudent(studentName)
    }

    fun getErrorData() : LiveData<String> {
        return errorData
    }

    override fun onCleared() {
        netDisposable.dispose()
        super.onCleared()
    }
}
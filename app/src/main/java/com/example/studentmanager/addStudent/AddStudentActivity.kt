package com.example.studentmanager.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.studentmanager.model.local.student.Student
import com.example.studentmanager.databinding.ActivityMain2Binding
import com.example.studentmanager.mainScreen.MainScreenViewModel
import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.local.MyDatabase
import com.example.studentmanager.util.*
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var addStudentViewModel: AddStudentViewModel
    private val compositeDisposable = CompositeDisposable()
    var isInserting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        addStudentViewModel = ViewModelProvider(
            this,
            AddStudentViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(AddStudentViewModel::class.java)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()


        val testMode = intent.getParcelableExtra<Student>(KEY)
        isInserting = (testMode == null)

        if (!isInserting) {
            logicUpdateStudent()
        }

        binding.btnDone.setOnClickListener {
            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun addNewStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            addStudentViewModel.insertNewStudent(
                Student(firstName + " " + lastName, course, score.toInt())
            )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("Insert finished :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("ERROR -> ${e.message ?: "null "}")
                    }

                })

        } else {
            showToast("Please enter complete information!!!")
        }
    }
    private fun updateStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            addStudentViewModel.updateNewStudent(
                Student(firstName + " " + lastName , course , score.toInt())
            )
                .asyncRequest()
                .subscribe(object  : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student information updated:)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("ERROR -> ${e.message ?: "null"}")
                    }

                })

        } else {
            showToast("Please enter complete information")
        }
    }
    private fun logicUpdateStudent() {
        binding.btnDone.text = "update"
        val dataFromIntent = intent.getParcelableExtra<Student>(KEY)

        binding.edtCourse.setText(dataFromIntent?.course ?: "")
        binding.edtScore.setText(dataFromIntent?.score?.toString() ?: "")

        val splittedName = dataFromIntent?.name?.split(" ")
        binding.edtFirstName.setText(splittedName?.getOrNull(0) ?: "")
        binding.edtLastName.setText(splittedName?.getOrNull(1) ?: "")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}
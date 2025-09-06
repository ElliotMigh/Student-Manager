package com.example.studentmanager.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanager.addStudent.AddStudentActivity
import com.example.studentmanager.databinding.ActivityMainBinding
import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.local.MyDatabase
import com.example.studentmanager.model.local.student.Student
import com.example.studentmanager.util.*
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: StudentAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var mainScreenViewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        compositeDisposable = CompositeDisposable()

        //initial recyclerview:
        initRecycler()

        mainScreenViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(MainScreenViewModel::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        mainScreenViewModel.getAllData().observe(this) {
            refreshRecyclerData(it)
        }

        mainScreenViewModel.getErrorData().observe(this) {
         Log.e("testLog",it)
        }
    }

    private fun refreshRecyclerData(newData: List<Student>) {
        myAdapter.refreshData(newData)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra(KEY, student)
        startActivity(intent)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Do you want to delete this item?")

        builder.setPositiveButton("Confirm") { dialog, which ->
            deleteFromServer(student, position)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initRecycler() {
        val myData = arrayListOf<Student>()
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun deleteFromServer(student: Student, position: Int) {
        mainScreenViewModel
            .removeStudent(student.name)
            .asyncRequest()
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    showToast("student removed :)")
                }

                override fun onError(e: Throwable) {
                    showToast("ERROR -> ${e.message ?: ""}")
                }

            })
    }

}
package com.example.studentmanager.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanager.addStudent.AddStudentActivity
import com.example.studentmanager.databinding.ActivityMainBinding
import com.example.studentmanager.model.MainRepository
import com.example.studentmanager.model.Student
import com.example.studentmanager.util.KEY
import com.example.studentmanager.util.asyncRequest
import com.example.studentmanager.util.showToast
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
        mainScreenViewModel = MainScreenViewModel(MainRepository())
        compositeDisposable = CompositeDisposable()


        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        compositeDisposable.add(
            mainScreenViewModel.progressBarSubject.subscribe {
                if (it) {
                    binding.progressMain.visibility = View.VISIBLE
                } else {
                    binding.progressMain.visibility = View.INVISIBLE
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        mainScreenViewModel.getAllStudent()
            .asyncRequest()
            .subscribe(object : SingleObserver<List<Student>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(t)
                }

                override fun onError(e: Throwable) {
                    showToast("ERROR -> ${e.message ?: "null"}")
                }

            })
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


    private fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
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

        myAdapter.removeItem(student, position)

    }

}
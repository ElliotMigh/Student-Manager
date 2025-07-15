package com.example.studentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanager.databinding.ActivityMain2Binding
import com.example.studentmanager.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.*
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

const val BASE_URL = "http://192.168.1.108:8081"
const val KEY = "student data"

class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: StudentAdapter
    lateinit var apiService: ApiService
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataFromApi()
    }

    private fun getDataFromApi() {
        apiService.getAllStudent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Student>> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(t)
                }

                override fun onError(e: Throwable) {
                    Log.v("testLog", e.message!!)
                }

            })
    }

    private fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
//        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//        dialog.contentText = "Delete this item?"
//        dialog.cancelText = "cancel"
//        dialog.confirmText = "confirm"
//        dialog.show()
//        dialog.setCancelClickListener {
//            dialog.dismiss()
//        }
//        dialog.setConfirmClickListener {
//            deleteFromServer(student, position)
//            dialog.dismiss()
//        }
    }

    private fun updateDataInServer(student: Student, position: Int) {
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra(KEY, student)
        startActivity(intent)
    }

    private fun deleteFromServer(student: Student, position: Int) {
        apiService.deleteStudent(student.name).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    myAdapter.removeItem(student, position)
                    Toast.makeText(this@MainActivity, "Deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error -> " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}
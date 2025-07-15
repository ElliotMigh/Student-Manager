package com.example.studentmanager.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.studentmanager.model.ApiService
import com.example.studentmanager.model.Student
import com.example.studentmanager.databinding.ActivityMain2Binding
import com.google.gson.JsonObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding:ActivityMain2Binding
    lateinit var apiService: ApiService
    var isInserting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val testMode = intent.getParcelableExtra<Student>(KEY)
        isInserting = (testMode == null)

        if (!isInserting) {
            binding.btnDone.text = "update"
            val dataFromIntent = testMode!!
            binding.edtCourse.setText(dataFromIntent.course.toString())
            binding.edtScore.setText(dataFromIntent.score.toString())

            val spilitedName = dataFromIntent.name.split(" ")
            binding.edtFirstName.setText(spilitedName[0])
            binding.edtLastName.setText(spilitedName.last())
        }
        binding.btnDone.setOnClickListener{
            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }
        }

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
            val jsonObject = JsonObject().apply {
                addProperty("name", "$firstName $lastName")
                addProperty("course", course)
                addProperty("score", score.toInt())
            }

            apiService.insertStudent(jsonObject).enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Insert finished :)", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this@MainActivity2, "Insert failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.v("testError", t.message ?: "Unknown error")
                    Toast.makeText(this@MainActivity2, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_LONG).show()
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
            val jsonObject = JsonObject().apply {
                addProperty("name", "$firstName $lastName")
                addProperty("course", course)
                addProperty("score", score.toInt())
            }

            apiService.updateStudent("$firstName $lastName", jsonObject).enqueue(object :
                Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Student updated successfully", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this@MainActivity2, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.v("testError", t.message ?: "Unknown error")
                    Toast.makeText(this@MainActivity2, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}
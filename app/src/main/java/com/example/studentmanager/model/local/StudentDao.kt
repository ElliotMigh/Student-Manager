package com.example.studentmanager.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.studentmanager.model.local.student.Student

@Dao
interface StudentDao {

    @androidx.room.Query("SELECT * FROM student")
    fun getAllData(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(student: Student)

    @Insert
    fun insertAll(student: List<Student>)

    @androidx.room.Query("DELETE FROM student WHERE name = :studentName")
    fun remove(studentName: String)
}
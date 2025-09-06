package com.example.studentmanager.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentmanager.model.local.student.Student
import com.example.studentmanager.model.local.student.StudentDao

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val studentDao: StudentDao

    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            // اگر دیتابیس قبلاً ساخته شده باشد همان را برمی‌گرداند
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                )
                    .fallbackToDestructiveMigration() // اگر نسخه عوض شد دیتابیس ریست می‌شود (اختیاری)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.example.studentmanager.util

import com.example.studentmanager.model.Student
import com.google.gson.JsonObject

fun studentToJsonObject(student: Student): JsonObject {

    val jsonObject = JsonObject()
    jsonObject.addProperty("name", student.name)
    jsonObject.addProperty("course", student.course)
    jsonObject.addProperty("score", student.score)

    return jsonObject
}
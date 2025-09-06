package com.example.studentmanager.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmanager.mainScreen.MainScreenViewModel
import com.example.studentmanager.model.MainRepository

class MainViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainScreenViewModel(mainRepository) as T
    }
}
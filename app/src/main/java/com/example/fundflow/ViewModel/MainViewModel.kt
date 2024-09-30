package com.example.fundflow.ViewModel

import androidx.lifecycle.ViewModel
import com.example.fundflow.Repository.MainRepository

class MainViewModel(val repository:MainRepository):ViewModel() {
    constructor():this(MainRepository())

    fun loadData() = repository.items
}
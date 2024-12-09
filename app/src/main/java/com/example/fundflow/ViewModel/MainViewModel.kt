package com.example.fundflow.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fundflow.Domain.ExpenseDomain
import com.example.fundflow.Repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _expenses = MutableLiveData<List<ExpenseDomain>>()
    val expenses: LiveData<List<ExpenseDomain>> get() = _expenses

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadExpenses() {
        viewModelScope.launch {
            try {
                val data = repository.fetchExpenses()
                _expenses.postValue(data)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Error fetching data")
            }
        }
    }
}

package com.example.employeemanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.employeemanagement.data.AppDatabase
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.repository.EmployeeRepository
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EmployeeRepository
    val allEmployees: LiveData<List<Employee>>
    val errorMessage = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        val employeeDao = AppDatabase.getDatabase(application).employeeDao()
        repository = EmployeeRepository(employeeDao)
        allEmployees = repository.allEmployees
    }

    fun getEmployeeById(id: Long) = viewModelScope.launch {
        isLoading.value = true
        try {
            repository.getEmployeeById(id)
            errorMessage.value = null
        } catch (e: Exception) {
            errorMessage.value = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading.value = false
        }
    }

    fun insertEmployee(employee: Employee) = viewModelScope.launch {
        isLoading.value = true
        try {
            repository.insertEmployee(employee)
            errorMessage.value = null
        } catch (e: Exception) {
            errorMessage.value = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading.value = false
        }
    }

    fun updateEmployee(employee: Employee) = viewModelScope.launch {
        isLoading.value = true
        try {
            repository.updateEmployee(employee)
            errorMessage.value = null
        } catch (e: Exception) {
            errorMessage.value = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading.value = false
        }
    }

    fun deleteEmployee(employee: Employee) = viewModelScope.launch {
        isLoading.value = true
        try {
            repository.deleteEmployee(employee)
            errorMessage.value = null
        } catch (e: Exception) {
            errorMessage.value = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading.value = false
        }
    }

    fun clearError() {
        errorMessage.value = null
    }

    suspend fun getEmployeeByIdDirect(id: Long): Employee? {
        return repository.getEmployeeById(id)
    }
} 
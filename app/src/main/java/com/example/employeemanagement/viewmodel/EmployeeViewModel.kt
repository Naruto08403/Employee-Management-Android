package com.example.employeemanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.employeemanagement.data.AppDatabase
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.repository.EmployeeRepository
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EmployeeRepository
    val allEmployees: LiveData<List<Employee>>

    init {
        val employeeDao = AppDatabase.getDatabase(application).employeeDao()
        repository = EmployeeRepository(employeeDao)
        allEmployees = repository.allEmployees
    }

    fun getEmployeeById(id: Long) = viewModelScope.launch {
        repository.getEmployeeById(id)
    }

    fun insertEmployee(employee: Employee) = viewModelScope.launch {
        repository.insertEmployee(employee)
    }

    fun updateEmployee(employee: Employee) = viewModelScope.launch {
        repository.updateEmployee(employee)
    }

    fun deleteEmployee(employee: Employee) = viewModelScope.launch {
        repository.deleteEmployee(employee)
    }
} 
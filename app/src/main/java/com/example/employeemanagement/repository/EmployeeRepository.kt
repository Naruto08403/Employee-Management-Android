package com.example.employeemanagement.repository

import androidx.lifecycle.LiveData
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.data.EmployeeDao

class EmployeeRepository(private val employeeDao: EmployeeDao) {
    val allEmployees: LiveData<List<Employee>> = employeeDao.getAllEmployees()

    suspend fun getEmployeeById(id: Long): Employee? {
        return employeeDao.getEmployeeById(id)
    }

    suspend fun insertEmployee(employee: Employee): Long {
        return employeeDao.insertEmployee(employee)
    }

    suspend fun updateEmployee(employee: Employee) {
        employeeDao.updateEmployee(employee)
    }

    suspend fun deleteEmployee(employee: Employee) {
        employeeDao.deleteEmployee(employee)
    }
} 
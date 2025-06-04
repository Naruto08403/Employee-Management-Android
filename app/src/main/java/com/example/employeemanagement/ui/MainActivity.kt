package com.example.employeemanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeemanagement.R
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.viewmodel.EmployeeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val viewModel: EmployeeViewModel by viewModels()
    private lateinit var adapter: EmployeeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupFab()
        observeEmployees()
    }

    private fun setupRecyclerView() {
        adapter = EmployeeListAdapter { employee ->
            val intent = Intent(this, EditEmployeeActivity::class.java).apply {
                putExtra(EditEmployeeActivity.EXTRA_EMPLOYEE_ID, employee.id)
            }
            startActivity(intent)
        }

        findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAddEmployee).setOnClickListener {
            startActivity(Intent(this, AddEmployeeActivity::class.java))
        }
    }

    private fun observeEmployees() {
        viewModel.allEmployees.observe(this) { employees ->
            adapter.submitList(employees)
        }
    }
} 
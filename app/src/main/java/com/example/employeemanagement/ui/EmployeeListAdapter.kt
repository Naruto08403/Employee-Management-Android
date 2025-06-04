package com.example.employeemanagement.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanagement.R
import com.example.employeemanagement.data.Employee

class EmployeeListAdapter(private val onItemClick: (Employee) -> Unit) :
    ListAdapter<Employee, EmployeeListAdapter.EmployeeViewHolder>(EmployeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_list_item, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = getItem(position)
        holder.bind(employee)
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val initialsTextView: TextView = itemView.findViewById(R.id.initialsTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val designationTextView: TextView = itemView.findViewById(R.id.designationTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(employee: Employee) {
            initialsTextView.text = employee.initials
            nameTextView.text = "${employee.firstName} ${employee.lastName}"
            designationTextView.text = employee.designation
        }
    }

    private class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }
} 
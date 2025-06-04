package com.example.employeemanagement.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagement.R
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.viewmodel.EmployeeViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.snackbar.Snackbar
import android.text.TextWatcher

class EditEmployeeActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_EMPLOYEE_ID = "extra_employee_id"
    }

    private val viewModel: EmployeeViewModel by viewModels()
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var designationEditText: TextInputEditText
    private lateinit var salaryEditText: TextInputEditText
    private var currentEmployee: Employee? = null
    private var lastDeletedEmployee: Employee? = null
    private lateinit var progressBar: android.widget.ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        progressBar = findViewById(R.id.progressBar)

        initializeViews()
        setupButtons()
        loadEmployeeData()
        observeErrorAndLoading()
        setupClearErrorOnEdit()
    }

    private fun initializeViews() {
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        addressEditText = findViewById(R.id.addressEditText)
        designationEditText = findViewById(R.id.designationEditText)
        salaryEditText = findViewById(R.id.salaryEditText)
    }

    private fun setupButtons() {
        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            if (validateInputs()) {
                updateEmployee()
            }
        }

        findViewById<MaterialButton>(R.id.deleteButton).apply {
            visibility = android.view.View.VISIBLE
            setOnClickListener {
                showDeleteConfirmationDialog()
            }
        }
    }

    private fun loadEmployeeData() {
        val employeeId = intent.getLongExtra(EXTRA_EMPLOYEE_ID, -1)
        if (employeeId == -1L) {
            Toast.makeText(this, "Invalid employee ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            currentEmployee = viewModel.getEmployeeByIdDirect(employeeId)
            withContext(Dispatchers.Main) {
                currentEmployee?.let { employee ->
                    firstNameEditText.setText(employee.firstName)
                    lastNameEditText.setText(employee.lastName)
                    emailEditText.setText(employee.email)
                    phoneEditText.setText(employee.phoneNumber)
                    addressEditText.setText(employee.address)
                    designationEditText.setText(employee.designation)
                    salaryEditText.setText(employee.salary.toString())
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (firstNameEditText.text.isNullOrBlank()) {
            firstNameEditText.error = getString(R.string.error_required_field)
            isValid = false
        }

        if (lastNameEditText.text.isNullOrBlank()) {
            lastNameEditText.error = getString(R.string.error_required_field)
            isValid = false
        }

        if (emailEditText.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        if (phoneEditText.text.isNullOrBlank() || !android.util.Patterns.PHONE.matcher(phoneEditText.text.toString()).matches()) {
            phoneEditText.error = getString(R.string.error_invalid_phone)
            isValid = false
        }

        if (addressEditText.text.isNullOrBlank()) {
            addressEditText.error = getString(R.string.error_required_field)
            isValid = false
        }

        if (designationEditText.text.isNullOrBlank()) {
            designationEditText.error = getString(R.string.error_required_field)
            isValid = false
        }

        if (salaryEditText.text.isNullOrBlank() || salaryEditText.text.toString().toDoubleOrNull() == null) {
            salaryEditText.error = getString(R.string.error_invalid_salary)
            isValid = false
        }

        return isValid
    }

    private fun updateEmployee() {
        currentEmployee?.let { employee ->
            val updatedEmployee = employee.copy(
                firstName = firstNameEditText.text.toString(),
                lastName = lastNameEditText.text.toString(),
                email = emailEditText.text.toString(),
                phoneNumber = phoneEditText.text.toString(),
                address = addressEditText.text.toString(),
                designation = designationEditText.text.toString(),
                salary = salaryEditText.text.toString().toDouble()
            )

            viewModel.updateEmployee(updatedEmployee)
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_employee)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                currentEmployee?.let { employee ->
                    lastDeletedEmployee = employee
                    viewModel.deleteEmployee(employee)
                    showUndoSnackbar()
                    Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    /**
     * Shows a Snackbar with Undo option after deleting an employee.
     */
    private fun showUndoSnackbar() {
        // Find a suitable view for Snackbar (use root view)
        val rootView = findViewById<android.view.View>(android.R.id.content)
        Snackbar.make(rootView, R.string.employee_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                lastDeletedEmployee?.let { employee ->
                    viewModel.insertEmployee(employee)
                    Toast.makeText(this, "Employee restored", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    /**
     * Observe error and loading states from ViewModel.
     */
    private fun observeErrorAndLoading() {
        viewModel.errorMessage.observe(this) { errorMsg ->
            errorMsg?.let {
                val rootView = findViewById<android.view.View>(android.R.id.content)
                Snackbar.make(rootView, it, Snackbar.LENGTH_LONG)
                    .setAction("Dismiss") { viewModel.clearError() }
                    .show()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading == true) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    /**
     * Clear error messages when user edits a field.
     */
    private fun setupClearErrorOnEdit() {
        val clearErrorListener = TextWatcherAdapter { viewModel.clearError() }
        firstNameEditText.addTextChangedListener(clearErrorListener)
        lastNameEditText.addTextChangedListener(clearErrorListener)
        emailEditText.addTextChangedListener(clearErrorListener)
        phoneEditText.addTextChangedListener(clearErrorListener)
        addressEditText.addTextChangedListener(clearErrorListener)
        designationEditText.addTextChangedListener(clearErrorListener)
        salaryEditText.addTextChangedListener(clearErrorListener)
    }
} 
package com.example.employeemanagement.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagement.R
import com.example.employeemanagement.data.Employee
import com.example.employeemanagement.viewmodel.EmployeeViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.snackbar.Snackbar

class AddEmployeeActivity : AppCompatActivity() {
    private val viewModel: EmployeeViewModel by viewModels()
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var designationEditText: TextInputEditText
    private lateinit var salaryEditText: TextInputEditText
    private lateinit var progressBar: android.widget.ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        progressBar = findViewById(R.id.progressBar)

        initializeViews()
        setupSaveButton()
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

    private fun setupSaveButton() {
        findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            if (validateInputs()) {
                saveEmployee()
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

    private fun saveEmployee() {
        val employee = Employee(
            firstName = firstNameEditText.text.toString(),
            lastName = lastNameEditText.text.toString(),
            email = emailEditText.text.toString(),
            phoneNumber = phoneEditText.text.toString(),
            address = addressEditText.text.toString(),
            designation = designationEditText.text.toString(),
            salary = salaryEditText.text.toString().toDouble()
        )

        viewModel.insertEmployee(employee)
        Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()
        finish()
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
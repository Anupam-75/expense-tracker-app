package com.example.monthlyexpensetracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.expensetracker.databinding.ActivityMainBinding
import com.example.monthlyexpensetracker.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: ExpenseDatabaseHelper
    private lateinit var expenseAdapter: ExpenseAdapter
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ExpenseDatabaseHelper(this)

        // Set up RecyclerView
        expenseAdapter = ExpenseAdapter(dbHelper.getAllExpenses(), dbHelper)
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExpenses.adapter = expenseAdapter

        // Add new expense
        binding.btnAddExpense.setOnClickListener {
            val description = binding.etDescription.text.toString()
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.etCategory.text.toString()

            val newExpense = Expense(0, description, amount, category, selectedDate)
            dbHelper.addExpense(newExpense)
            expenseAdapter.updateExpenses(dbHelper.getAllExpenses())
            clearInputs()
        }

        // Date picker for selecting date
        binding.etDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Navigate to monthly expense view
        binding.btnViewMonthlyExpenses.setOnClickListener {
            startActivity(Intent(this, MonthlyExpenseActivity::class.java))
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedYear-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
            binding.etDate.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun clearInputs() {
        binding.etDescription.text?.clear()
        binding.etAmount.text?.clear()
        binding.etCategory.text?.clear()
        binding.etDate.text?.clear()
        selectedDate = ""
    }
}

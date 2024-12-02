package com.example.monthlyexpensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.expensetracker.databinding.ActivityMonthlyExpenseBinding
import com.example.monthlyexpensetracker.databinding.ActivityMonthlyExpenseBinding

class MonthlyExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthlyExpenseBinding
    private lateinit var dbHelper: ExpenseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ExpenseDatabaseHelper(this)
        val expensesByMonth = dbHelper.getExpensesGroupedByMonth()

        binding.recyclerViewMonthlyExpenses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMonthlyExpenses.adapter = MonthlyExpenseAdapter(expensesByMonth)
    }
}

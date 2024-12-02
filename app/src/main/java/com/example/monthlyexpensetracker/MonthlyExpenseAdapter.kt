package com.example.monthlyexpensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.monthlyexpensetracker.databinding.ItemMonthlyExpenseBinding

class MonthlyExpenseAdapter(private val monthlyExpenses: Map<String, List<Expense>>) :
    RecyclerView.Adapter<MonthlyExpenseAdapter.MonthlyExpenseViewHolder>() {

    private val sortedMonths = monthlyExpenses.keys.sortedDescending()

    inner class MonthlyExpenseViewHolder(private val binding: ItemMonthlyExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(month: String, expenses: List<Expense>) {
            binding.tvMonth.text = month
            binding.recyclerViewExpenses.adapter = ExpenseAdapter(expenses, ExpenseDatabaseHelper(binding.root.context))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyExpenseViewHolder {
        val binding = ItemMonthlyExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthlyExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyExpenseViewHolder, position: Int) {
        val month = sortedMonths[position]
        val expenses = monthlyExpenses[month] ?: emptyList()
        holder.bind(month, expenses)
    }

    override fun getItemCount(): Int = sortedMonths.size
}

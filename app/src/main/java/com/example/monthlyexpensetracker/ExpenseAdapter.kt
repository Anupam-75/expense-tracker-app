package com.example.monthlyexpensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private var expenseList: List<Expense>,
    private val dbHelper: ExpenseDatabaseHelper
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.tvDescription.text = expense.description
        holder.tvAmount.text = expense.amount.toString()
        holder.tvCategory.text = expense.category
        holder.tvDate.text = expense.date

        holder.btnDelete.setOnClickListener {
            dbHelper.deleteExpense(expense.id)
            expenseList = dbHelper.getAllExpenses()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = expenseList.size

    fun updateExpenses(newExpenses: List<Expense>) {
        expenseList = newExpenses
        notifyDataSetChanged()
    }
}

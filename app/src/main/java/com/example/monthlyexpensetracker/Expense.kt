package com.example.monthlyexpensetracker

data class Expense(
    val id: Int,
    val description: String,
    val amount: Double,
    val category: String,
    val date: String
)

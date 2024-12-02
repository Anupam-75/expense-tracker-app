package com.example.monthlyexpensetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "expenses.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "expenses"
        const val COL_ID = "id"
        const val COL_DESCRIPTION = "description"
        const val COL_AMOUNT = "amount"
        const val COL_CATEGORY = "category"
        const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_DESCRIPTION TEXT, " +
                "$COL_AMOUNT REAL, " +
                "$COL_CATEGORY TEXT, " +
                "$COL_DATE TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addExpense(expense: Expense) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DESCRIPTION, expense.description)
            put(COL_AMOUNT, expense.amount)
            put(COL_CATEGORY, expense.category)
            put(COL_DATE, expense.date)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COL_DATE DESC", null)
        if (cursor.moveToFirst()) {
            do {
                expenses.add(
                    Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return expenses
    }

    fun getExpensesGroupedByMonth(): Map<String, List<Expense>> {
        val expensesByMonth = mutableMapOf<String, MutableList<Expense>>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COL_DATE DESC",
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val expense = Expense(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                val month = expense.date.substring(0, 7)
                if (!expensesByMonth.containsKey(month)) {
                    expensesByMonth[month] = mutableListOf()
                }
                expensesByMonth[month]?.add(expense)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return expensesByMonth
    }

    fun deleteExpense(expenseId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(expenseId.toString()))
        db.close()
    }
}

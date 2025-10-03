package com.lsp.dailchampion.data.Local.Expense

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 2, exportSchema = true)
abstract  class ExpenseDataBase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
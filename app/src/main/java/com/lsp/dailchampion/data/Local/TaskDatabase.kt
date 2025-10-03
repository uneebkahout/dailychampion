package com.lsp.dailchampion.data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lsp.dailchampion.data.Local.Expense.Expense
import com.lsp.dailchampion.data.Local.Expense.ExpenseDao
import com.lsp.dailchampion.data.Local.Task.Task
import com.lsp.dailchampion.data.Local.Task.TaskDao

@Database(entities = [Task::class, Expense ::class], version = 2, exportSchema = true)
abstract  class  DaliChampionDatabase : RoomDatabase(){
    abstract  fun tasKDao(): TaskDao
    abstract fun expenseDao(): ExpenseDao

}
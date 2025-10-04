package com.lsp.dailchampion.data.Local.Expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense( expense: Expense): Long

    @Query("SELECT * FROM expense_table where date =:date  ")
    fun getExpenses(date: String): Flow<List<Expense>>
    @Delete
    suspend fun deleteExpense(expense: Expense): Int;

}
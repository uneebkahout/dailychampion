package com.lsp.dailchampion.data.Local.Expense

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense( expense: Expense): Long

}
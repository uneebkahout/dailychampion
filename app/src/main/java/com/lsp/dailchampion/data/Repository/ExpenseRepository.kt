package com.lsp.dailchampion.data.Repository

import com.lsp.dailchampion.data.Local.Expense.Expense
import com.lsp.dailchampion.data.Local.Expense.ExpenseDao
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun upsertExpense(expense: Expense): Long{
       return expenseDao.upsertExpense(expense = expense)
    }
}
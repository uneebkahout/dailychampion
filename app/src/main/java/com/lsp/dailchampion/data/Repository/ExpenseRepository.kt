package com.lsp.dailchampion.data.Repository

import com.lsp.dailchampion.Presentaion.ViewModel.Expense.ExpenseList
import com.lsp.dailchampion.data.Local.Expense.Expense
import com.lsp.dailchampion.data.Local.Expense.ExpenseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun upsertExpense(expense: Expense): Long{
       return expenseDao.upsertExpense(expense = expense)
    }
    fun getExpense(date: String): Flow<List<ExpenseList>>{
      return   expenseDao.getExpenses(date = date).map{ entities->
             entities.map { ExpenseList(id = it.id, expenseCategory = it.expenseCategory, expenseDescription = it.expenseDescription, expenseAmount = it.expenseAmount) }
         }
    }

    suspend fun deleteExpense(expense: Expense): Int{
        return  expenseDao.deleteExpense(expense = expense)
    }
}
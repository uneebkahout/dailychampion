package com.lsp.dailchampion.ViewModel.Expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lsp.dailchampion.Presentaion.Screen.Expense.Expenses
import com.lsp.dailchampion.data.Local.Expense.Expense
import com.lsp.dailchampion.data.Repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private  val expenseRepository: ExpenseRepository
) : ViewModel(){
//    <================================ Expense Data State ===================== >
    private  val _expenseState  = MutableStateFlow(ExpenseState())
    val expenseState = _expenseState.asStateFlow()


    fun addExpense(){
        viewModelScope.launch {
            try {
                val expense = Expense(expenseCategory = expenseState.value.expenseCategory, expenseAmount = expenseState.value.expenseAmount, expenseDescription = expenseState.value.expenseDescription)
                val response = expenseRepository.upsertExpense(expense = expense)
                if (response>0){
                    clearState()
                }
            }catch (e: Exception){
                Log.d("TAG"," Error ${e.localizedMessage}")
            }
        }
    }


    fun updateExpenseCategory(expenseCategory: String){
        _expenseState.update { it->
            it.copy(
                expenseCategory = expenseCategory
            )
        }

    }

    fun  updateExpenseAmount(expenseAmount: Double){
        _expenseState.update { it->
                it.copy(
                    expenseAmount=expenseAmount
                )
        }
    }
    fun  updateExpenseDescription(expenseDescription:String){
        _expenseState.update { it->
            it.copy(
                expenseDescription = expenseDescription
            )
        }
    }

    fun toggleExpenseDropDown(){
        _expenseState.update { it->
            it.copy(
                showExpenseDropDown =   ! it.showExpenseDropDown
            )
        }
    }

    fun clearState(){
        _expenseState.update { it->
            it.copy(
                expenseAmount=0.0,
                expenseDescription = "",
                expenseCategory = ""

            )
        }
    }
}

data class ExpenseState(
    val expenseCategory :String="",
    val expenseAmount: Double=0.0,
    val expenseDescription :String="",


    val showExpenseDropDown: Boolean =false
)
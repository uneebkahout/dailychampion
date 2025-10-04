package com.lsp.dailchampion.Presentaion.ViewModel.Expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsp.dailchampion.data.Local.Expense.Expense
import com.lsp.dailchampion.data.Repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _expenseState = MutableStateFlow(ExpenseState())
    val expenseState: StateFlow<ExpenseState> = _expenseState.asStateFlow()

    fun addExpense() {
        viewModelScope.launch {
            try {
                val expense = Expense(
                    expenseCategory = expenseState.value.expenseCategory,
                    expenseAmount = expenseState.value.expenseAmount,
                    expenseDescription = expenseState.value.expenseDescription,
                    date = expenseState.value.expenseDate
                )
                val response = expenseRepository.upsertExpense(expense)
                if (response > 0) {
                    clearState()
                }
            } catch (e: Exception) {
                Log.e("ExpenseVM", "Error saving expense: ${e.localizedMessage}")
            }
        }
    }

    fun updateExpense() {
        toggleEditLoading()
        viewModelScope.launch {
            val request = Expense(
                id = expenseState.value.expenseID,
                expenseCategory = expenseState.value.expenseCategory,
                expenseDescription =  expenseState.value.expenseDescription,
                date = expenseState.value.expenseDate,
                expenseAmount = expenseState.value.expenseAmount
            )
            val response = expenseRepository.upsertExpense(expense = request)
            if (response>0){
                toggleEditLoading()
                clearState()
                toggleEditExpenseDialogue()
            }
            else{
                toggleEditLoading()
            }
        }
    }
    fun deleteExpense(){
        toggleExpenseDeleteLoading()
        viewModelScope.launch {
            val request  = Expense(
                id =expenseState.value.expenseID ,
                expenseCategory =expenseState.value.expenseCategory,
                expenseDescription = expenseState.value.expenseDescription,
                expenseAmount = expenseState.value.expenseAmount,
                date = expenseState.value.expenseDate
            )
            val response = expenseRepository.deleteExpense(expense = request)
            if (response>0){
                toggleExpenseDeleteLoading()
                clearState()
                toggleShowExpenseDelete()
            }else{
                toggleExpenseDeleteLoading()
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val expenses: StateFlow<List<ExpenseList>> =
        expenseState.map { it.expenseDate }
            .distinctUntilChanged()
            .flatMapLatest { date ->
                expenseRepository.getExpense(date)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // 🔹 Update State
    fun updateExpenseCategory(expenseCategory: String) {
        _expenseState.update { it.copy(expenseCategory = expenseCategory) }
    }

    fun updateExpenseAmount(expenseAmount: Double) {
        _expenseState.update { it.copy(expenseAmount = expenseAmount) }
    }

    fun updateExpenseID(id:Int){
        _expenseState.update { it->
            it.copy(
                expenseID = id
            )
        }
    }
    fun updateExpenseDescription(expenseDescription: String) {
        _expenseState.update { it.copy(expenseDescription = expenseDescription) }
    }

    fun updateExpenseDate(date: String) {
        _expenseState.update { it.copy(expenseDate = date) }
    }

    fun toggleExpenseDropDown() {
        _expenseState.update { it.copy(showExpenseDropDown = !it.showExpenseDropDown) }
    }

    fun toggleDatePicker() {
        _expenseState.update { it.copy(showExpenseDatePicker = !it.showExpenseDatePicker) }
    }
    fun toggleShowExpenseDelete(){
        _expenseState.update { it->
            it.copy(
                showDeleteExpense = !it.showDeleteExpense
            )
        }
    }
    fun toggleExpenseDeleteLoading(){
            _expenseState.update{it->
                it.copy(
                    deleteLoading =  !it.deleteLoading
                )
            }
    }
    fun toggleEditExpenseDialogue(){
        _expenseState.update { it->
            it.copy(
showEditDialogue = ! it.showEditDialogue
            )
        }
    }

    fun  toggleEditLoading(){
        _expenseState.update { it->
            it.copy(
                editingLoading = ! it.editingLoading
            )
        }
    }
    fun clearState() {
        _expenseState.update {
            it.copy(
                expenseAmount = 0.0,
                expenseDescription = "",
                expenseCategory = ""
            )
        }
    }

    // 🔹 Filter & Total
    fun setExpense(expense: List<ExpenseList>) {
        _expenseState.update { state ->
            val filtered = applyFilter(state.selectedCategory, expense)
            state.copy(
                filteredExpenses = filtered,
                totalExpense = filtered.sumOf { it.expenseAmount }
            )
        }
    }

    fun changeCategory(category: String) {
        _expenseState.update { state ->
            val filtered = applyFilter(category, state.filteredExpenses.ifEmpty { emptyList() })
            state.copy(
                selectedCategory = category,
                filteredExpenses = filtered,
                totalExpense = filtered.sumOf { it.expenseAmount }
            )
        }
    }

    fun selectExpense(expense: ExpenseList?) {
        _expenseState.update { it.copy(selectedExpense = expense) }
    }

    private fun applyFilter(category: String, expenses: List<ExpenseList>): List<ExpenseList> {
        return if (category == "All") expenses
        else expenses.filter { it.expenseCategory == category }
    }
}


data class ExpenseState(
    val expenseCategory :String="",
    val expenseAmount: Double=0.0,
    val expenseDescription :String="",
    val expenseDate:String = "",
    val expenseID: Int = 0,


//    val expenses: List<ExpenseList> = emptyList(),
    val selectedCategory: String = "All",
    val filteredExpenses: List<ExpenseList> = emptyList(),
    val totalExpense: Double = 0.0,
    val selectedExpense: ExpenseList? = null,

    val showExpenseDropDown: Boolean =false,
    val showExpenseDatePicker: Boolean =false,
    val showDeleteExpense : Boolean = false,
    val deleteLoading :Boolean=false,
    val showEditDialogue: Boolean =false,
    val editingLoading: Boolean = false,
)

data class  ExpenseList(
    val id:Int,
    val expenseCategory:String,
    val expenseAmount: Double,
    val expenseDescription: String
)
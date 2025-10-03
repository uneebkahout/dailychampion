package com.lsp.dailchampion.data.Local.Expense

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true)var  id: Int = 0,
    val expenseCategory :String ,
    val expenseAmount: Double,
    val expenseDescription :String,
    val date:String,
)

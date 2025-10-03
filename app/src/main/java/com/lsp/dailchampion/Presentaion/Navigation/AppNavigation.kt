package com.lsp.dailchampion.Presentaion.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

import com.lsp.dailchampion.Presentaion.Screen.DailyTask.DailyTask
import com.lsp.dailchampion.Presentaion.Screen.Expense.AddExpenseScreen
import com.lsp.dailchampion.Presentaion.Screen.Expense.ExpenseScreen
import com.lsp.dailchampion.Presentaion.Screen.HomePage

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(AppRoutes.ExpenseScreen)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()} ,
        entryProvider = entryProvider{
            entry<AppRoutes.HomeScreen>{
                HomePage(
                    navigateToDailyTask={
                        backStack.add(AppRoutes.DailyTask)
                    }
                )

            }
            entry<AppRoutes.DailyTask>{
                DailyTask()

            }
            entry<AppRoutes.ExpenseScreen>{
                ExpenseScreen(
                    onBack = {
                        if (backStack.isNotEmpty()){
                            backStack.removeAt(backStack.size-1)
                        }
                    }
                )

            }
        }
    )

}
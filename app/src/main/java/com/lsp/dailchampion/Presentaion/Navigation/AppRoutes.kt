package com.lsp.dailchampion.Presentaion.Navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed  class AppRoutes: NavKey {
@Serializable
data object  HomeScreen : AppRoutes()
@Serializable
data object  DailyTask : AppRoutes()
 @Serializable
 data object  ExpenseScreen : AppRoutes()

 }
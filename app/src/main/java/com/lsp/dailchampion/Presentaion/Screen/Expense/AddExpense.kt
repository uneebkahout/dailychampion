package com.lsp.dailchampion.Presentaion.Screen.Expense

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.Presentaion.Services.showNotification
import com.lsp.dailchampion.ViewModel.Expense.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen() {
    // Local states
    val expenseViewModel : ExpenseViewModel = hiltViewModel()
    val expenseState by expenseViewModel.expenseState.collectAsState()
val focusManger = LocalFocusManager.current
    // Sample categories
    val categories = listOf("Food", "Travel", "Rent", "Utilities", "Shopping", "Other")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManger.clearFocus()
                    })
                }
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // --- Category Dropdown ---
            ExposedDropdownMenuBox(
                expanded = expenseState.showExpenseDropDown,
                onExpandedChange = { expenseViewModel.toggleExpenseDropDown() }
            ) {
                OutlinedTextField(
                    value = expenseState.expenseCategory,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expenseState.showExpenseDropDown)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expenseState.showExpenseDropDown,
                    onDismissRequest = { expenseViewModel.toggleExpenseDropDown() },
                    modifier = Modifier.width(200.dp)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                             expenseViewModel.updateExpenseCategory(category)
                                expenseViewModel.toggleExpenseDropDown()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Expense Amount ---
            OutlinedTextField(
                value = expenseState.expenseAmount.toString(),
                onValueChange = { expenseViewModel.updateExpenseAmount(expenseAmount = it.toDouble()) },
                label = { Text("Expense Amount") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Expense Description ---
            OutlinedTextField(
                value = expenseState.expenseDescription,
                onValueChange = { expenseViewModel.updateExpenseDescription(expenseDescription = it) },
                label = { Text("Expense Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Save Button ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {

                Button(
                    onClick = {
                        expenseViewModel.addExpense()

                    },
                    modifier = Modifier.width(150.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("Add Expense")
                }
            }
        }
    }
}

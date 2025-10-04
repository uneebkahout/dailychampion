package com.lsp.dailchampion.Presentaion.Screen.Expense


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.R
import com.lsp.dailchampion.Presentaion.ViewModel.Expense.ExpenseList
import com.lsp.dailchampion.Presentaion.ViewModel.Expense.ExpenseViewModel
import com.lsp.dailchampion.ui.theme.Poppins
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseScreen(onBack:()-> Unit) {
    val tabTitles = listOf("Add Expense", "Expense")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val viewModel : ExpenseViewModel = hiltViewModel();
    val taskState by viewModel.expenseState.collectAsState();

    val dateDialogSate = rememberMaterialDialogState();
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    val selectedDate =taskState.expenseDate?.takeIf {
        it.isNotBlank()
    }?.let {
        try {
            LocalDate.parse(it,inputFormatter)
        }catch (e: Exception){
            null
        }
    }
    val formattedDate = selectedDate?.format(outputFormatter)

    LaunchedEffect(taskState.showExpenseDatePicker) {
        if (taskState.showExpenseDatePicker){
            dateDialogSate.show();
        }
    }
    LaunchedEffect(Unit) {
        viewModel.updateExpenseDate(date = LocalDate.now().toString())
    }

    MaterialDialog(
        dialogState =dateDialogSate,
        buttons = {
            positiveButton(
                "Ok"
            ) {
                viewModel.toggleDatePicker()
            }
            negativeButton("Cancel") {
                viewModel.toggleDatePicker()
            }
        }
    ) {
        datepicker { date->

            viewModel.updateExpenseDate(date=date.toString());
        }
    }
    Scaffold { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(

                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row( verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {onBack()}) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                            }
                            Text(
                                text = "Daily Expense",
                                fontSize = 15.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = formattedDate ?:"Select Date")
                            IconButton(onClick = {
                                viewModel.toggleDatePicker()
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.schedule),
                                    contentDescription = "Pick Date",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        containerColor = MaterialTheme.colorScheme.surface,
                        indicator = { tabPositions ->
                            Box(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(4.dp)
                                    .padding(start = 15.dp, end = 15.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = Poppins
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color(0xFFFFFFFF), // your custom color

                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (selectedTabIndex) {
                    0 -> AddExpenseScreen(

                    )
                    1 -> Expenses(

                    )

                }
            }


        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun Expenses() {
    val expenseViewModel: ExpenseViewModel = hiltViewModel()
    val state by expenseViewModel.expenseState.collectAsState()
    val expenseList by expenseViewModel.expenses.collectAsState()

    // Keep filtered list and total in sync with VM
    LaunchedEffect(expenseList) {
        expenseViewModel.setExpense(expenseList)
    }

    when {
        state.filteredExpenses.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No Expense Found")
            }
        }

        else -> {
            ExpenseTable(
                expenses = state.filteredExpenses,
                selectedCategory = state.selectedCategory,
                totalExpense = state.totalExpense,
                selectedExpense = state.selectedExpense,
                onCategoryChange = { expenseViewModel.changeCategory(it) },
                onEditClick = { expenseViewModel.selectExpense(it) },
                onDeleteClick = { expenseViewModel.toggleShowExpenseDelete() },
                onCloseDialog = { expenseViewModel.selectExpense(null) }
            )
            if(state.showDeleteExpense){
                DeleteConfirmationDialog(
                    showDialog = state.showDeleteExpense,
                    onDismiss = {expenseViewModel.toggleShowExpenseDelete()
                    }
                )
            }
        }
    }
}


@Composable
fun ExpenseTable(
    expenses: List<ExpenseList>,
    selectedCategory: String,
    totalExpense: Double,
    selectedExpense: ExpenseList?,
    onCategoryChange: (String) -> Unit,
    onEditClick: (ExpenseList) -> Unit = {},
    onDeleteClick: () -> Unit ,
    onCloseDialog: () -> Unit
) {
    // Extract unique categories
    val categories = listOf("All") + expenses.map { it.expenseCategory }.distinct()
    val expenseViewModel :ExpenseViewModel    =hiltViewModel()
    val expenseState by expenseViewModel.expenseState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        // 🔹 Filter Dropdown
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text("Filter by Category: ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))

            var expanded by remember { mutableStateOf(false) }

            Box {
                Text(
                    text = selectedCategory,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                onCategoryChange(category)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // 🔹 Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .background(Color(0xFF004AAD)) // header background
        ) {
            TableHeaderCell("Category", Modifier.weight(1.2f))
            TableHeaderCell("Amount", Modifier.width(120.dp))
            TableHeaderCell("Actions", Modifier.width(80.dp))
        }

        // 🔹 Table Rows (scrollable)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            items(expenses) { expense ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                ) {
                    TableCell(
                        text = expense.expenseCategory,
                        modifier = Modifier.weight(1.2f),
                        textColor = Color(0xFF052A6C),
                        fontWeight = FontWeight.SemiBold
                    )

                    TableCell(
                        text = "Rs. ${expense.expenseAmount}",
                        modifier = Modifier.width(120.dp),
                        textColor = Color(0xFF004AAD),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .border(1.dp, Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { onEditClick(expense) }) {
                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = "View",
                                tint = Color(0xFF00796B)
                            )
                        }
                    }
                }
            }

            // 🔹 Total Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    TableCell(
                        text = "TOTAL",
                        modifier = Modifier.weight(1.2f),
                        textColor = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    TableCell(
                        text = "Rs. $totalExpense",
                        modifier = Modifier.width(120.dp),
                        textColor = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

    }

// 🔹 View Expense Dialog
    if (selectedExpense != null) {
        AlertDialog(
            onDismissRequest = { onCloseDialog() },
            title = {
                Text(selectedExpense.expenseCategory, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Amount: Rs. ${selectedExpense.expenseAmount}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Description:", fontWeight = FontWeight.SemiBold)
                    Text(selectedExpense.expenseDescription)
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                        onEditClick(selectedExpense)
                        onCloseDialog()
                    }) {
                        Text("Edit", color = Color(0xFF004AAD))
                    }
                    TextButton(onClick = {
                        onDeleteClick()
                        expenseViewModel.updateExpenseCategory(expenseCategory = selectedExpense.expenseCategory)
                        expenseViewModel.updateExpenseAmount(expenseAmount = selectedExpense.expenseAmount)
                        expenseViewModel.updateExpenseDescription(expenseDescription = selectedExpense.expenseDescription)
                        expenseViewModel.updateExpenseDate(date =expenseState.expenseDate)
                        expenseViewModel.updateExpenseID(id = selectedExpense.id)



                        onCloseDialog()
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                    TextButton(onClick = { onCloseDialog() }) {
                        Text("Close")
                    }
                }
            }
        )
    }
}


@Composable
fun TableHeaderCell(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray)
            .background(Color(0xFF052A6C))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun TableCell(
    text: String,
    modifier: Modifier,
    textColor: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray)
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = fontWeight,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    ) {
    val expenseViewModel : ExpenseViewModel = hiltViewModel()
    val expenseState by expenseViewModel.expenseState.collectAsState()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this company?") },
            confirmButton = {
                Button(
                    onClick = {
                        if (!expenseState.deleteLoading ) {
                            expenseViewModel.deleteExpense()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !expenseState.deleteLoading
                ) {
                    if (expenseState.deleteLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Deleting...", color = Color.White)
                    } else {
                        Text("Delete", color = Color.White)
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface
                    ),
                    enabled = !expenseState.deleteLoading
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }



}





@Composable
fun EditExpenseDialogue() {

    val focusManager = LocalFocusManager.current
        val expenseViewModel : ExpenseViewModel = hiltViewModel()
    val expenseState by expenseViewModel.expenseState.collectAsState()
    if (expenseState.showEditDialogue) {
        AlertDialog(
            onDismissRequest = {
                expenseViewModel.toggleEditExpenseDialogue()
                expenseViewModel.clearState()
            },
            title = {
                Text(if (expenseState.editingLoading) "Update Company" else "Add Company")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                ) {
                    OutlinedTextField(
                        value = uiState.companyName,
                        onValueChange = { companyViewModel.updateCompanyName(it) },
                        label = { Text("Company Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFF052A6C),
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.LightGray,
                            errorBorderColor = Color.Magenta
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.saleDiscount,
                        onValueChange = { companyViewModel.updateSaleDiscount(it) },
                        label = { Text("Sale Discount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFF052A6C),
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.LightGray,
                            errorBorderColor = Color.Magenta
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.purchaseDiscount,
                        onValueChange = { companyViewModel.updatePurchaseDiscount(it) },
                        label = { Text("Purchase Discount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFF052A6C),
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.LightGray,
                            errorBorderColor = Color.Magenta
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = { companyViewModel.updateDescription(it) },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFF052A6C),
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.LightGray,
                            errorBorderColor = Color.Magenta
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Error / Loading messages
                    when {
                        uiState.isEditing -> {
                            if (updateCompanyState is UpdateCompanyState.Error) {
                                val error = updateCompanyState as UpdateCompanyState.Error
                                Text(
                                    text = error.message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                LaunchedEffect(error) {
                                    companyViewModel.clearUpdateCompanyMessage()
                                }
                            }
                        }
                        else -> {
                            if (companyState is CompanyState.Error) {
                                val error = companyState as CompanyState.Error
                                Text(
                                    text = error.message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                LaunchedEffect(error) {
                                    companyViewModel.clearAddCompanyMessage()
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                val isLoading =
                    (expenseState.editingLoading )

                Button(
                    onClick = {
                       expenseViewModel.updateExpense()
                    },
                    enabled = expenseState.expenseCategory.isNotBlank() &&
                            expenseState.expenseAmount.toString().isNotBlank() &&
                            expenseState.expenseDescription.isNotBlank() &&
                            !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Saving...", color = Color.White)
                    } else {
                        Text(if (expenseState.editingLoading) "Update" else "Save")
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        companyViewModel.toggleShowAlertDialogue()
                        companyViewModel.clearState()
                    },
                    enabled = !(
                            (uiState.isEditing && updateCompanyState is UpdateCompanyState.Loading) ||
                                    (!uiState.isEditing && companyState is CompanyState.Loading)
                            )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
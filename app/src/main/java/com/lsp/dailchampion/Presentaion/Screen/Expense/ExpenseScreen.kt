package com.lsp.dailchampion.Presentaion.Screen.Expense


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.Presentaion.Screen.DailyTask.AddTask
import com.lsp.dailchampion.Presentaion.Screen.DailyTask.CompletedTask
import com.lsp.dailchampion.Presentaion.Screen.DailyTask.TodayTask
import com.lsp.dailchampion.R
import com.lsp.dailchampion.ViewModel.MyViewModel
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
    val viewModel : MyViewModel = hiltViewModel();
    val taskState by viewModel.taskState.collectAsState();

    val dateDialogSate = rememberMaterialDialogState();
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    val selectedDate =taskState.todayDate?.takeIf {
        it.isNotBlank()
    }?.let {
        try {
            LocalDate.parse(it,inputFormatter)
        }catch (e: Exception){
            null
        }
    }
    val formattedDate = selectedDate?.format(outputFormatter)

    LaunchedEffect(taskState.showDatePicker) {
        if (taskState.showDatePicker){
            dateDialogSate.show();
        }
    }
    LaunchedEffect(Unit) {
        viewModel.updateTodayDate(date = LocalDate.now().toString())
    }

    MaterialDialog(
        dialogState =dateDialogSate,
        buttons = {
            positiveButton(
                "Ok"
            ) {
                viewModel.toggleShowDatePicker()
            }
            negativeButton("Cancel") {
                viewModel.toggleShowDatePicker()
            }
        }
    ) {
        datepicker { date->

            viewModel.updateTodayDate(date=date.toString());
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
                        Text(
                            text = "Daily Expense",
                            fontSize = 20.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium
                        )
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = formattedDate ?:"Select Date")
                            IconButton(onClick = {
                                viewModel.toggleShowDatePicker()
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

@Composable
fun Expenses() {

}
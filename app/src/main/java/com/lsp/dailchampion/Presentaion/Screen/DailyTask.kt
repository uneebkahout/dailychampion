package com.lsp.dailchampion.Presentaion.Screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import com.lsp.dailchampion.ui.theme.Poppins
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.R
import com.lsp.dailchampion.ViewModel.MyViewModel
import com.lsp.dailchampion.ViewModel.TaskDataState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun DailyTask() {
    val tabTitles = listOf("Add Task", "Task", "Completed Task")
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
                            text = "Daily Task",
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
                    0 -> AddTask(
                        viewModel = viewModel,
                        taskState =taskState
                    )
                    1 -> TodayTask(
                        viewModel=viewModel
                    )
                    2 -> CompletedTask(viewModel = viewModel)
                }
            }


        }
    }
}

@Composable
fun AddTask(
    viewModel: MyViewModel,
    taskState: TaskDataState,
    ) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {




                OutlinedTextField(
                    value = taskState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    placeholder = {
                        Text(
                            text = "Enter Title",
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    label = {
                        Text(
                            text = "Title",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)) // rounded corners
                        .padding(vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = Poppins,
                        fontSize = 14.sp
                    ),
                    shape = RoundedCornerShape(12.dp)
                )


                OutlinedTextField(
                    value = taskState.description ,
                    onValueChange = { viewModel.updateDescription(it) },
                    placeholder = { Text("Enter Description", fontFamily = Poppins) },
                    label = { Text("Description", fontFamily = Poppins) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                enabled = taskState.title.isNotBlank() || taskState.todayDate.isNotBlank() || taskState.description.isNotBlank(),
                onClick = {viewModel.clearState()},
                modifier = Modifier.width(120.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "Clear Task")
            }
            Button(
                enabled = taskState.title.isNotBlank() && taskState.todayDate.isNotBlank(),
                onClick = {viewModel.createTask()},
                modifier = Modifier.width(120.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "Add Task")
            }

        }

            }






    }









@Composable
fun EditTaskDialogue(
    viewModel: MyViewModel,

    onDismiss :  () -> Unit
) {
    val focusManager = LocalFocusManager.current;
    val taskState by viewModel.taskState.collectAsState()
    AlertDialog(
        modifier = Modifier. pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })},
        onDismissRequest = {
            onDismiss()
        },
        title = {Text(text = "Edit Text", fontFamily = Poppins)},
        confirmButton = {
            TextButton(onClick = {
                viewModel.updateTask()
                onDismiss()
            }) {
                Text(text = "Save", fontFamily = Poppins)
            }

        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.updateTitle(title = "")
                viewModel.updateDescription(description = "")
                onDismiss()
            }) {
                Text(text = "Cancel", fontFamily = Poppins)
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ){
                Column {
                    OutlinedTextField(
                        value = taskState.title,
                        onValueChange = { viewModel.updateTitle(it) },
                        placeholder = {
                            Text(
                                text = "Enter Title",
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        label = {
                            Text(
                                text = "Title",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)) // rounded corners
                            .padding(vertical = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = taskState.description ,
                        onValueChange = { viewModel.updateDescription(it) },
                        placeholder = { Text("Enter Description", fontFamily = Poppins) },
                        label = { Text("Description", fontFamily = Poppins) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

        }

    )
}


@Composable
fun DeleteTask(
    viewModel: MyViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = {onDismiss()},
        title = {
            Text(text = "Delete Task")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteTask();
                    onDismiss();
                }
            ) {
                Text(text = "Delete", fontFamily = Poppins)
            }

        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel", fontFamily = Poppins)

            }

        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Are you sure you to want to delete Task")
            }

        }

    )

}
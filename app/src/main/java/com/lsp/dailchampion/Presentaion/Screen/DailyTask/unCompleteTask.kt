package com.lsp.dailchampion.Presentaion.Screen.DailyTask

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lsp.dailchampion.Presentaion.ViewModel.MyViewModel
import com.lsp.dailchampion.Presentaion.ViewModel.TaskList

@Composable
fun TodayTask(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel,
) {
    val taskList by viewModel.taskListByDate.collectAsState()
    val selectedTaskID by viewModel.selectedTaskID.collectAsState()
    LaunchedEffect(taskList) {
        Log.d("TAG","TASK LIST $taskList")
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(taskList) { task ->
//            val isExpanded = expandedTaskId == task.id
            UnCompleteTaskCard(task=task, viewModel = viewModel);

        }
        item {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                if (selectedTaskID.isNotEmpty()){
                    IconButton(onClick = {
                        viewModel.completeTask()
                    },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
                            .shadow(4.dp, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirm",
                            tint = Color.White
                        )
                    }
                }

            }
        }
    }

}
@Composable
fun UnCompleteTaskCard(
    task: TaskList,
    viewModel: MyViewModel
) {
    val selectedTaskID by viewModel.selectedTaskID.collectAsState();
    var isExpanded by remember { mutableStateOf(false) }
    var showEditDialogue by remember { mutableStateOf(false) }
    var showDeleteDialogue by remember { mutableStateOf(false) }
    val selectedTask = selectedTaskID.contains(task.id);


    // Animated colors
            val backgroundColor by animateColorAsState(
                if (selectedTask) Color(0xFFBBDEFB) else Color.White,
                label = "cardBackground"
            )
            val borderColor = if (isExpanded) Color(0xFF1976D2) else Color.LightGray


    Card(
        modifier = Modifier
            .fillMaxWidth()
                    .clickable {
                            viewModel.toggleTaskID(taskID = task.id )
                    }
                    .border(
                        width = if (isExpanded) 2.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .animateContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isExpanded) 10.dp else 4.dp
                )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF191C24),
                    maxLines = if (isExpanded) 3 else 1, // ✅ keep 1 line when collapsed
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f) // ✅ take remaining space only
                )

                // Eye button
                IconButton(onClick = {
                    isExpanded = !isExpanded
                }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Details",
                        tint = Color.Gray
                    )
                }
            }


            if (isExpanded) {
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Description
                Text(
                    text = task.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            showEditDialogue = true
                            viewModel.updateID(id = task.id)
                            viewModel.updateTitle(title = task.title)
                            viewModel.setTaskPriority(task = task.taskPriority)
                            viewModel.updateDescription(description = task.description)
                        },
                        modifier = Modifier
                            .background(Color(0xFF0D6EFD), shape = CircleShape)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            showDeleteDialogue = true
                            viewModel.updateID(id = task.id)
                            viewModel.updateTitle(title = task.title)
                            viewModel.setTaskPriority(task = task.taskPriority)
                            viewModel.updateDescription(description = task.description)
                        },
                        modifier = Modifier
                            .background(Color(0xFFAB2E3C), shape = CircleShape)
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    // Dialogues
    if (showEditDialogue) {
        EditTaskDialogue(
            viewModel = viewModel,
            onDismiss = { showEditDialogue = false }
        )
    }
    if (showDeleteDialogue) {
        DeleteTask(viewModel = viewModel, onDismiss = { showDeleteDialogue = false })
    }
}













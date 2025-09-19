package com.lsp.dailchampion.Presentaion.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.ViewModel.MyViewModel


import com.lsp.dailchampion.ViewModel.TaskList

@Composable
fun TaskSliderRow(tasks: List<TaskList>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks) { task ->
            TaskCard(task = task)
        }
    }
}
@Composable
fun TaskCard(task: TaskList) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (task.isCompleted)
                            listOf(Color(0xFF84fab0), Color(0xFF8fd3f4))
                        else
                            listOf(Color(0xFFFFD6A5), Color(0xFFFFA69E))
                    )
                )
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.Schedule,
                        contentDescription = "Task Status",
                        tint = if (task.isCompleted) Color(0xFF2ECC71) else Color(0xFF34495E),
                        modifier = Modifier.size(20.dp)
                    )
                }


            }
        }
    }
}



@Composable
fun TaskSliderScreen() {
  val viewModel: MyViewModel = hiltViewModel()
    val homeScreenTask by viewModel.homeScreenTask.collectAsState()

    Column {
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        TaskSliderRow(tasks = homeScreenTask)
    }
}
package com.lsp.dailchampion.Presentaion.Screen.DailyTask


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lsp.dailchampion.ViewModel.MyViewModel
import com.lsp.dailchampion.ViewModel.TaskList


@Composable
fun CompletedTask(viewModel: MyViewModel) {
    val completeTask by viewModel.completedTask.collectAsState()

    LazyColumn (

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(completeTask){task->

            CompleteTaskCard(task = task);

        }

    }


}

@Composable
fun CompleteTaskCard(  task: TaskList) {
    var isExpanded by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        Color.White,
        label = "cardBackground"
    )
    val borderColor = if (isExpanded) Color(0xFF1976D2) else Color.LightGray
    Card(
        modifier = Modifier
            .fillMaxWidth()

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
    ){
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


}
}
}
}
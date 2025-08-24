package com.lsp.dailchampion.Presentaion.Screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lsp.dailchampion.R
import com.lsp.dailchampion.ViewModel.Feature
import com.lsp.dailchampion.ViewModel.MyViewModel
import com.lsp.dailchampion.ui.theme.Poppins


@Composable
fun HomePage(
    navigateToDailyTask : () -> Unit
) {
    val viewModel : MyViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.updateGreetingMessage()
    }
    Scaffold(

    ){innerPadding->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
//                < ================= HEADER ================= >
               Header()
                Spacer(modifier = Modifier.height(10.dp))
                TaskSliderScreen()
                Spacer(modifier = Modifier.height(20.dp))
        HomeTab(navigateToDailyTask = {navigateToDailyTask()})
            }
            }
        }
    }




@Composable
fun Header() {
val viewModel : MyViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 50.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row {
                Image(painter = painterResource(id = R.drawable.profile),
                    modifier = Modifier.size(50.dp),
                    contentDescription = null)
                Spacer(modifier = Modifier.width(20.dp))
                Column {

                    Text(text = "${state.greetingMessage}", fontSize = 16.sp , fontFamily = Poppins , fontWeight = FontWeight.Medium, )
                    Text(text = "Uneeb Ahmed", fontFamily = Poppins , fontWeight = FontWeight.Normal, )

                }
            }
            Row {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
            }
        }
        }
        }

@Composable
fun HomeTab(navigateToDailyTask: () -> Unit) {
    val viewModel : MyViewModel = hiltViewModel()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            FeatureCard(
                name = "Daily Task",
                color = Color(0xFF6C63FF),
                motivation = "Conquer your day.",
                emoji = "🗓️",
                onClick = {
                    navigateToDailyTask()
                }
            )
        }
        item {
            FeatureCard(
                name = "Expenses",
                color = Color(0xFFFF7043),
                motivation = "Spend smartly",
                emoji = "💸",
                onClick = {}
            )
        }
        item {
            FeatureCard(
                name = "Diary",
                color = Color(0xFF42A5F5),
                motivation = "Reflect to grow",
                emoji = "📔",
                onClick = {}
            )
        }

        item {
            FeatureCard(
                name = "Goals",
                color = Color(0xFF6C63FF),
                motivation = "Track your progress.",
                emoji = "🎯",
                onClick = {}
            )
        }
        }
    }

@Composable
fun FeatureCard(
    name:String,
    color: Color,
    motivation: String,
    emoji:String,
    progress: Float = 1.0f, // 70% by default
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "CardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                        onClick()
                    }
                )
            }
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        color.copy(alpha = 0.2f),
                        color.copy(alpha = 0.4f)
                    )
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.07f),
                RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress Ring with Emoji
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 5.dp,
                    color = color,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title + Motivation
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontFamily = Poppins,

                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
                Text(
                    text = motivation,
//                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontFamily = Poppins,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}












package com.example.space_station.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.input.pointer.pointerInput
import com.example.space_station.R
import com.example.space_station.ui.theme.LoadingScene1
import com.example.space_station.ui.theme.LoadingScene2
import com.example.space_station.ui.theme.customFontFamily
import com.example.space_station.ui.theme.textFontFamily
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun LoadingPage(onNavigateToMainPage: () -> Unit) {
    val circleRadius = remember { mutableStateOf(0.dp) }
    val startPosition = remember { mutableStateOf(Offset(0f, 0f)) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(LoadingScene1, LoadingScene1, LoadingScene1, LoadingScene2),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .pointerInput(Unit) {

                    onNavigateToMainPage()

                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(100.dp))
                Image(
                    painter = painterResource(id = R.drawable.space_station),
                    contentDescription = "My Image",
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(200.dp))
                Text(
                    text = "우주 정거장",
                    style = TextStyle(
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 70.sp,
                        color = Color(0xFFB9C2FF),
                        letterSpacing = 1.5.sp
                    ),
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "아무 곳이나 클릭하세요",
                    style = TextStyle(
                        fontFamily = textFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color(0xFFB9C2FF),
                        letterSpacing = 1.5.sp
                    )
                )
            }

        }
    }
}


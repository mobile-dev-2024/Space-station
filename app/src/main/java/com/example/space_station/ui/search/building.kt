package com.example.space_station.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.space_station.ui.theme.CardColors
import com.example.space_station.ui.theme.Primary
import com.example.space_station.viewmodel.LectureTimetable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Buildings(
    lectureTimetable: LectureTimetable,
    modifier: Modifier = Modifier,
    navigator: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("빈 강의실 찾기") },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White,   // 제목 텍스트 색상
                    actionIconContentColor = Color.White // 액션 아이콘 색상
                ),
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(lectureTimetable.buildings.value) { buildingName ->
                BuildingCard(buildingName, onClick = {
                    lectureTimetable.setSelectedBuilding(buildingName)
                    navigator()
                })
            }
        }
    }
}

@Composable
private fun BuildingCard(
    buildingName: String,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize(),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = CardColors // 카드 컨테이너 색상
        ),
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ){
            Text(buildingName, style = typography.titleLarge)
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Settings",
            )
        }
    }
}
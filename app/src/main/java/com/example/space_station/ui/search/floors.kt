package com.example.space_station.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.space_station.viewmodel.LectureTimetable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Floors(
    lectureTimetable: LectureTimetable,
    backNavigator: () -> Unit = {},
    navigator: () -> Unit = {},
) {
    val building = lectureTimetable.selectedBuilding.value
    val allRooms = lectureTimetable.getAllRoomsByBuilding(building)
    val usedRooms = lectureTimetable.getUsedRooms(
        building = building,
        week = lectureTimetable.getNowKoreanDayOfWeek(),
        time = lectureTimetable.getNowKoreanTime(),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(building) },
                navigationIcon = {
                    IconButton(onClick = backNavigator) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                }
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
            allRooms.forEach { (floor, rooms) ->
                item{
                    FloorCard(
                        floor = floor,
                        total = rooms.size,
                        emptyCnt = rooms.size - (usedRooms[floor]?.distinctBy { it[12] }?.size ?: 0),
                        onClick = {
                            lectureTimetable.setSelectedFloor(floor, rooms, usedRooms[floor] ?: emptyList<List<String>>())
                            navigator()
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun FloorCard(
    floor: String,
    total: Int,
    emptyCnt: Int,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 텍스트와 바 게이지 영역 (2/3)
            Column(
                modifier = Modifier
                    .weight(4f) // Row에서 2/3 차지
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$floor 층",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 간격 조정
                    Text(
                        text = "빈 강의실 : $emptyCnt/$total",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // 텍스트와 바 게이지 간 간격

                // 바 게이지 (임시로 Box로 구성)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(emptyCnt.toFloat()/total) // 사용량에 따라 크기를 조정
                            .fillMaxHeight()
                            .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                    )
                }
            }

            // 아이콘 영역 (1/3)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Click",
                modifier = Modifier
                    .weight(1f) // Row에서 1/3 차지
                    .size(24.dp)
                    .padding(start = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
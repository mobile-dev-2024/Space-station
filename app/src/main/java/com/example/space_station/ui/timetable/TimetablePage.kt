package com.example.space_station.ui.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.ui.theme.BackgroundColor
import com.example.space_station.ui.theme.MainContainerColor
import com.example.space_station.ui.theme.Pink80
import com.example.space_station.ui.theme.Purple40
import com.example.space_station.ui.theme.PurpleGrey80


data class TimetableSubject(
    val day: String, //월 화 와 같이 한글자
    val startHour: Int,
    val startMinute : Int,
    val endHour: Int,
    val endMinute : Int,
    val name: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetablePage(
    onAddButtonClicked : () -> Unit,
    onSettingClicked : () -> Unit,
) {
    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("시간표") },
                actions = {
                    IconButton(
                        onClick = onAddButtonClicked,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            tint = Color.White,
                            imageVector = Icons.Outlined.AddBox,
                            contentDescription = "Add timetable"
                        )
                    }
                    IconButton(onClick = onSettingClicked) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            tint = Color.White,
                            imageVector = Icons.Outlined.Settings, contentDescription = "Setting"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = Color.White,
                ),
            )
        }
    ) { innerPadding ->

        val subjects = listOf(
            TimetableSubject("월", 9, 30, 11, 0, "모바일 앱 개발"),
            TimetableSubject("화", 10, 0, 12, 0, "IOT 프로젝트"),
            TimetableSubject("수", 13, 30, 15, 30, "ACT")
        )

        val endTime = 18
        val times = (8..endTime).flatMap { hour ->
            listOf(if (hour > 12) "${hour - 12}" else "$hour","  ")
        }
        val days = listOf("월", "화", "수", "목", "금")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
                .padding(bottom = 16.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 요일 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Spacer(modifier = Modifier.width(20.dp))

                days.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .width(77.dp)
//                            .weight(1f)
                            .padding(bottom = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 시간표 본체
            times.forEachIndexed { rowIndex, time ->

                val currentHour = 8 + rowIndex / 2
                val currentMinute = if (rowIndex % 2 == 0) 0 else 30

                Row(modifier = Modifier.fillMaxWidth()) {
                    // 시간 열
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(50.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            text = time,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    // 시간표 셀
                    days.forEach { day ->
                        val currentSubject = subjects.find {
                            it.day == day &&
                                    ((currentHour > it.startHour || (currentHour == it.startHour && currentMinute >= it.startMinute)) &&
                                            (currentHour < it.endHour || (currentHour == it.endHour && currentMinute < it.endMinute)))
                        }

                        Box(
                            modifier = Modifier
//                                .weight(1f)
                                .width(77.dp)
                                .height(50.dp)
                                .background(if (currentSubject != null) Pink80 else Color.Transparent)
                                .border(0.5.dp, if (currentSubject != null) Pink80 else Color.Gray),

                        ) {
                            // 과목 이름 표시
                            currentSubject?.takeIf { currentHour == it.startHour && currentMinute == it.startMinute }?.let {
                                Text(
                                    text = it.name,
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

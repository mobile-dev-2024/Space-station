package com.example.space_station.ui.search

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.core.NotificationService
import com.example.space_station.viewmodel.LectureTimetable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rooms(
    lectureTimetable: LectureTimetable,
    notificationService: NotificationService,
    backNavigator: () -> Unit = {},
) {
    val building = lectureTimetable.selectedBuilding.value
    val floor = lectureTimetable.selectedFloor.value
    val rooms = lectureTimetable.selectedFloorRooms.value.sortedBy { it.second }
    val userRoomsByFloor = lectureTimetable.selectedFloorUsedRooms.value.sortedBy { it[12] }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(building + " " +floor + "층") },
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
            rooms.forEach { (floor, item) ->
                val state = userRoomsByFloor.filter { it[12] == item }.isEmpty()
                val professor = userRoomsByFloor.filter { it[12] == item }.map { it[6] }.firstOrNull() ?: ""
                val lecture = userRoomsByFloor.filter { it[12] == item }.map { it[4] }.firstOrNull() ?: ""
                val lectureEndTime = userRoomsByFloor.filter { it[12] == item }.map { it[10] }.firstOrNull() ?: ""
                item {
                    RoomCard(
                        room = item ,
                        onClick = {},
                        state = state,
                        professor = professor,
                        lecture = lecture,
                        lectureEndTime = lectureEndTime,
                        checkIn = { notificationService.showBasicNotification(
                            title = "퇴실 알림",
                            content = "10분 뒤에 $building ${item}에서 수업이 시작됩니다. 퇴실해주세요."
                        ) },
                        nextLectureTime = {
                            lectureTimetable.getNextLectureTime(building, floor, item, lectureTimetable.getNowKoreanTime(), lectureTimetable.getNowKoreanDayOfWeek())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: String,
    state: Boolean = false,
    professor: String = "",
    lecture: String = "",
    lectureEndTime: String = "",
    checkIn:  () -> Unit = {},
    onClick: () -> Unit = {},
    nextLectureTime : () -> String,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize(),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ){
                if (state) {
                    val nextLectureTimeString = nextLectureTime()
                    Column {
                        Text(room, style = typography.titleLarge)
                    }
                } else {
                    Text(room, style = typography.titleLarge)
                }
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                if (state) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("빈 강의실", style = typography.titleMedium)
                        if (nextLectureTime() == "") {
                            Text("다음 수업 없음", fontSize = 12.sp)
                        } else {
                            Text("다음 수업 ${nextLectureTime()}", fontSize = 12.sp)
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("수업 중", style = typography.titleMedium)
                        Text(lectureEndTime,  fontSize = 12.sp)
                    }
                }
            }
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                if (state) {
                    if (nextLectureTime() == "") {
                        Button(
                            onClick = {},
                        ) {
                            Text("자유입실")
                        }
                    } else {
                        Button(
                            onClick = checkIn,
                        ) {
                            Text("입실하기")
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(if (lecture.length >5) lecture.substring(0,5) + "..." else lecture, style = typography.titleMedium)
                        Text(professor,  fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
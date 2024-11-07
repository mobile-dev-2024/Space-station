package com.example.space_station.ui.search

import android.R.attr.onClick
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.space_station.viewmodel.LectureTimetable
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rooms(
    lectureTimetable: LectureTimetable,
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
                val nextLectureTime = lectureTimetable.getNextLectureTime(building, floor, item, lectureTimetable.getNowKoreanTime(), lectureTimetable.getNowKoreanDayOfWeek())
                item {
                    RoomCard(
                        room = item ,
                        onClick = {},
                        state = state,
                        professor = professor,
                        lecture = lecture,
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
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            if (state) {
                val nextLectureTimeString = nextLectureTime()
                Column {
                    Text(room, style = typography.titleLarge)
                    if (nextLectureTimeString == "") {
                        Text("다음 수업 없음")
                    } else {
                        Text("다음 수업 ${nextLectureTimeString}")
                    }
                }
            } else {
                Text(room, style = typography.titleLarge)
            }
            if (state) {
                Text("공강")
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(lecture)
                    Text(professor)
                }
            }
            Button(
                onClick = { /*TODO*/ },
            ) { }
        }
    }
}
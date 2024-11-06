package com.example.space_station.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.space_station.viewmodel.LectureTimetable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rooms(
    lectureTimetable: LectureTimetable,
    backNavigator: () -> Unit = {},
) {
    val building = lectureTimetable.selectedBuilding.value
    val floor = lectureTimetable.selectedFloor.value
    var rooms = lectureTimetable.selectedFloorRooms.value

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
        ) {
            rooms.forEach { (floor, item) ->
               item {
                   RoomCard(room = item, onClick = {})
               }
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: String,
    onClick: () -> Unit = {},
) {
    Text(text = room)
}
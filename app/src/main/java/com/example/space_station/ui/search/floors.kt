package com.example.space_station.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ElevatedCard
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
import org.w3c.dom.Text
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Floors(
    lectureTimetable: LectureTimetable,
    backNavigator: () -> Unit = {},
    navigator: () -> Unit = {},
) {
    val building = lectureTimetable.selectedBuilding.value
    val allRooms = lectureTimetable.getAllRoomsByBuilding(building)
//    val usedRooms = lectureTimetable.getUsedRooms(building, )

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
        ) {
            allRooms.forEach { (floor, rooms) ->
                item{
                    FloorCard(
                        floor = floor,
                        onClick = {
                            lectureTimetable.setSelectedFloor(floor, rooms)
                            navigator()
                        }
                    )
                }
            }
        }

    }
}

@Composable
private fun FloorCard(
    floor: String,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize(),
        onClick = onClick,
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ){
            Text(floor)
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Click",
            )
        }
    }
}
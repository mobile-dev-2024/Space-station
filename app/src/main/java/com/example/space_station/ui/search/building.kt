package com.example.space_station.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.space_station.ui.theme.SpacestationTheme
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
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ){
            Text(buildingName)
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Settings",
            )
        }
    }
}
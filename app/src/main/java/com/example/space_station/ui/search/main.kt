package com.example.space_station.ui.search

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.space_station.ui.timetable.AddTimetablePage
import com.example.space_station.ui.timetable.TimetablePage

import com.example.space_station.viewmodel.LectureTimetable

//서치페이지의 메인 페이지

@Composable
fun SearchMain(
    lectureTimetable: LectureTimetable,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "timeTable"
    ) {
        composable(route = "buildings") {
            Buildings(
                lectureTimetable = lectureTimetable,
                navigator = { navController.navigate("floors") }
            )
        }
        composable(route = "floors") {
            Floors(
                lectureTimetable = lectureTimetable,
                backNavigator = { navController.navigateUp() },
                navigator = { navController.navigate("rooms") }
            )
        }
        composable(route = "rooms") {
            Rooms(
                lectureTimetable = lectureTimetable,
                backNavigator = { navController.navigateUp() }
            )
        }
    }
}
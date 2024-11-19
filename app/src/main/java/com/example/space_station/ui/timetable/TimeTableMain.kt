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

import com.example.space_station.viewmodel.TimeTableModel

//서치페이지의 메인 페이지

@Composable
fun TimeTableMain(
    timeTableModel: TimeTableModel,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "timeTable"
    ) {
        composable(route = "timeTable") {
            val subjects by timeTableModel.subjects.observeAsState(emptyList())
            TimetablePage(
                subjects = subjects,
                onAddButtonClicked = {
                    navController.navigate(route = "addTimeTable")
                },
                onSettingClicked = {},
                onRemoveSubject = { timeTableModel.removeSubject(it) }
            )
        }
        composable(
            route = "addTimeTable",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it }
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it }
                ) + fadeOut()
            }
        ) {
            AddTimetablePage(
                backNavigator = { navController.navigateUp() },
                getLecturesBySubject = { query, searchType ->
                    timeTableModel.getLecturesBySubject(query, searchType)
                },
                addLectureToTimetable = { subject ->
                    timeTableModel.addSubject(subject)
                }
            )
        }
    }
}
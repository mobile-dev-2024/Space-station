package com.example.space_station.navigation

import SettingPage
import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.FirstBaseline
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.space_station.core.NotificationService
import com.example.space_station.firebase.FirebaseManager
import com.example.space_station.ui.auth.AuthenticationManager
import com.example.space_station.ui.auth.LoginPage
import com.example.space_station.ui.main.LoadingPage

import com.example.space_station.ui.main.MainPage
import com.example.space_station.ui.search.SearchMain
import com.example.space_station.ui.search.TimeTableMain
import com.example.space_station.viewmodel.BookMarkModel
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.viewmodel.TimeTableModel
import com.example.space_station.viewmodel.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PageManager(
    context: Context
) {
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    val userViewModel = viewModel<UserViewModel>()

    val lectureTimetableViewModel = viewModel<LectureTimetable>()
    val timeTableViewModel = viewModel<TimeTableModel>()
    lectureTimetableViewModel.loadExcelData(context)
    timeTableViewModel.loadExcelData(context)
//                timeTableViewModel.loadUserTimeTableFromDB() // 안에 firebase에서 주는 coursecode List<String> 넣어야 함

    // 알림 권한 요청
    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val notificationService = NotificationService(context)
    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
    val bookMarkModel = viewModel<BookMarkModel>()

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = if (isLoading) "LoadingPage" else if (isLoggedIn) "MainPage" else "LoginPage") {
        composable("LoadingPage") {
            LoadingPage(onClick = {
                isLoading = false
            })
        }

        composable("LoginPage") {
            AuthenticationManager(
                onLoginSuccess = { isLoggedIn = true },
                userViewModel = userViewModel,
            )
        }

        composable("MainPage") {
            when(currentPage){
                0-> MainPage(
                    timeTableModel = timeTableViewModel,
                    currentPage = currentPage,
                    onClick = { x: Int -> currentPage = x },
                    onSettingClick = {
                        navController.navigate("SettingPage")
                    }
                )
                1->SearchMain(
                    currentPage = currentPage,
                    onClick = {x:Int -> currentPage = x},
                    lectureTimetable = lectureTimetableViewModel,
                    notificationService = notificationService,
                    bookMarkModel = bookMarkModel,
                    onSettingClick = {
                        navController.navigate("SettingPage")
                    }
                )
                2-> TimeTableMain(
                    userViewModel = userViewModel,
                    currentPage = currentPage,
                    onClick = {currentPage = it},
                    onSettingClick = {
                        navController.navigate("SettingPage")
                    },
                    timeTableModel = timeTableViewModel
                )
            }
        }

        composable("SettingPage") {
            SettingPage(
                userViewModel = userViewModel,
                onBackClick = { navController.navigateUp() },
                onLogoutClick = {
                    // 로그아웃 처리
                },
                onDeleteAccountClick = {
                    // 계정 삭제 처리
                }
            )
        }
    }
}

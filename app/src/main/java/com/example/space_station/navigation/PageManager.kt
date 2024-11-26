package com.example.space_station.navigation

import SettingPage
import android.Manifest
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import java.util.UUID

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PageManager(
    context: Context
) {
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var isBackground by rememberSaveable { mutableStateOf(false) }
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
    val user = FirebaseManager.instance.getCurrentUser()
    if (user != null) {
        Log.d("user",user.uid.toString())
    }
    if(user!=null) {

        FirebaseManager.instance.getUserSettingData(
            uid = user.uid.toString(),
            onSuccess = {
                Log.d("LoginCheck",it.toString())
                userViewModel.updateUid(user.uid)
                userViewModel.updateUserSettingData(it)

                //로그인 하고 세팅데이터 불러 오면 렉쳐테이블 뷰모델에 데이터 업로드 함
                val uuid = userViewModel.userSettingData.value.uuid
                lectureTimetableViewModel.updateFirebaseDataToApp(
                    checkedInRoom = userViewModel.userSettingData.value.room,
                    uuid = if (uuid != "") {
                        UUID.fromString(uuid)} else {null},
                    //뷰모델 안의 함수를 다른 뷰모델 안에서 호출하기 어렵기 때문에 여기서 함수를 넘겨줌
                    roomFunc = { userViewModel.updateCheckInRoom(it) },
                    uuidFunc = { userViewModel.updateCheckInRoomPushID(it) }
                )
                // 북마크 모델에 데이터 업로드 함
                bookMarkModel.updateFirebaseDataToApp(
                    bookMark = userViewModel.userSettingData.value.bookmarks,
                    updateFireBase = { userViewModel.updateBookmark(it) }
                )
                timeTableViewModel.loadUserTimeTableFromDB(userViewModel.userSettingData.value.timetable)
                isLoggedIn = true
                isBackground = true
                Log.d("backgroundWork",isBackground.toString())
            },
            onError = {
                isBackground = true


            }
        )

    }
    else{
        isBackground = true
        Log.d("backgroundWork",isBackground.toString())
    }


    NavHost(navController = navController, startDestination =if (!isBackground)"LoadingPage" else if (isLoading) "LoadingPage" else if (isLoggedIn) "MainPage" else "LoginPage") {
        composable("LoadingPage") {
            LoadingPage(onClick = {
                isLoading = false
            })
        }

        composable("LoginPage") {
            AuthenticationManager(
                onLoginSuccess = { isLoggedIn = true
                                 Log.d("Login","success")},
                userViewModel = userViewModel,
                lectureTimetable = lectureTimetableViewModel,
                bookMarkModel = bookMarkModel,
                timeTableModel = timeTableViewModel
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
                    timeTableModel = timeTableViewModel,
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
                    FirebaseManager.instance.signOut()
                    isLoggedIn = false
                },
                onDeleteAccountClick = {
                    FirebaseManager
                    // 계정 삭제 처리
                }
            )
        }
    }
}

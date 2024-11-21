package com.example.space_station

import android.Manifest // 추가
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.space_station.core.NotificationService
import com.example.space_station.navigation.PageManager
import com.example.space_station.ui.main.MainPage
import com.example.space_station.ui.search.Buildings
import com.example.space_station.ui.search.SearchMain
import com.example.space_station.ui.theme.SpacestationTheme
import com.example.space_station.viewmodel.BookMarkModel
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.ui.search.SearchMain
import com.example.space_station.ui.search.TimeTableMain
import com.example.space_station.viewmodel.TimeTableModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpacestationTheme {
                // 강의 시간표 데이터 로드
                val lectureTimetableViewModel = viewModel<LectureTimetable>()
                val timeTableViewModel = viewModel<TimeTableModel>()
                lectureTimetableViewModel.loadExcelData(this)
                timeTableViewModel.loadExcelData(this)
//                timeTableViewModel.loadUserTimeTableFromDB() // 안에 firebase에서 주는 coursecode List<String> 넣어야 함

                // 알림 권한 요청
                val postNotificationPermission =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                val notificationService = NotificationService(this)
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }

                val bookMarkModel = viewModel<BookMarkModel>()

//                PageManager()
                SearchMain(
                    lectureTimetable = lectureTimetableViewModel,
                    notificationService = notificationService,
                    bookMarkModel = bookMarkModel
                )

            }
        }
    }
}
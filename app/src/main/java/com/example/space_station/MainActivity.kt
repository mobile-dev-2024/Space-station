package com.example.space_station

import android.Manifest // 추가
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.space_station.core.NotificationService
import com.example.space_station.ui.search.SearchMain
import com.example.space_station.ui.theme.SpacestationTheme
import com.example.space_station.viewmodel.LectureTimetable
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
                val postNotificationPermission =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                val notificationService = NotificationService(this)

                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }

                val lectureTimetableViewModel = viewModel<LectureTimetable>()
                lectureTimetableViewModel.loadExcelData(this)

                SearchMain(
                    lectureTimetable = lectureTimetableViewModel,
                    notificationService = notificationService
                )


//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding),
//                        lectureTimetable = lectureTimetableViewModel
//                    )
//                }
            }
        }
    }
}
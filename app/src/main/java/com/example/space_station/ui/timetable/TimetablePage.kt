package com.example.space_station.ui.timetable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.ui.layout.BottomBarComponent
import com.example.space_station.ui.theme.BackgroundColor
import com.example.space_station.ui.theme.PastelBlue
import com.example.space_station.ui.theme.PastelGreen
import com.example.space_station.ui.theme.PastelYellow
import com.example.space_station.ui.theme.Pink80
import com.example.space_station.ui.theme.Purple40
import com.example.space_station.ui.theme.Purple80
import com.example.space_station.ui.theme.PurpleGrey80
import com.example.space_station.viewmodel.TimetableSubject
import com.example.space_station.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetablePage(
    userViewModel: UserViewModel,
    currentPage :Int,
    onClick:(Int)->Unit,

    subjects: List<TimetableSubject>,
    onAddButtonClicked: () -> Unit,
    onSettingClicked: () -> Unit,
    onRemoveSubject: (TimetableSubject) -> Unit
) {
    val colorList = listOf(Pink80, Purple80, PurpleGrey80, PastelBlue, PastelGreen,  PastelYellow)
    var selectedSubject by remember { mutableStateOf<TimetableSubject?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    // 과목명에 따른 색상 매핑을 저장할 Map 생성
//    val subjectColorMap = remember {
//        subjects
//            .map { it.name }
//            .distinct()
//            .mapIndexed { index, name ->
//                name to colorList[index % colorList.size]
//            }
//            .toMap()
//    }
    val subjectColorMap by remember(subjects) {
        derivedStateOf {
            subjects
                .map { it.name }
                .distinct()
                .mapIndexed { index, name ->
                    name to colorList[index % colorList.size]
                }
                .toMap()
        }
    }


    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("시간표") },
                actions = {
                    IconButton(
                        onClick = onAddButtonClicked,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            tint = Color.White,
                            imageVector = Icons.Outlined.AddBox,
                            contentDescription = "Add timetable"
                        )
                    }
                    IconButton(
                        onClick = onSettingClicked,
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = Color.White,
                ),
            )
        },
        bottomBar = {
            BottomBarComponent(
                currentPage =currentPage,
                onClick = onClick
            )
        }

    ) { innerScaffoldPadding ->
        val endTime = 18
        val times = (8..endTime).flatMap { hour ->
            listOf(if (hour > 12) "${hour - 12}" else "$hour", " ")
        }
        val days = listOf("월", "화", "수", "목", "금")


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerScaffoldPadding)
                .padding(8.dp)
                .padding(bottom = 16.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
                .background(BackgroundColor)
        ) {
            // 요일 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                days.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .width(77.dp)
                            .padding(bottom = 4.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 시간표 본체
            times.forEachIndexed { rowIndex, time ->
                val currentHour = 8 + rowIndex / 2
                val currentMinute = if (rowIndex % 2 == 0) 0 else 30

                Row(modifier = Modifier.fillMaxWidth()) {
                    // 시간 열의 높이도 변경
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(45.dp),  // 50.dp에서 35.dp로 변경
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            text = time,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }

                    // 시간표 셀
                    days.forEach { day ->
                        val currentSubject = subjects.find {
                            it.day == day &&
                                    ((currentHour > it.startHour || (currentHour == it.startHour && currentMinute >= it.startMinute)) &&
                                            (currentHour < it.endHour || (currentHour == it.endHour && currentMinute < it.endMinute)))
                        }

                        // 색상 지정 부분 수정
                        val color = currentSubject?.let { subject ->
                            subjectColorMap[
                                subject.name]
                        } ?: Color.Transparent

                        Box(
                            modifier = Modifier
                                .width(77.dp)
                                .height(45.dp)
                                .weight(1f)// 50.dp에서 35.dp로 변경
                                .background(color)
                                .border(0.5.dp, if (currentSubject != null) color else Color.Gray)
                                .clickable {
                                    currentSubject?.let {
                                        selectedSubject = it
                                        showBottomSheet = true
                                    }
                                }
                        ) {
                            currentSubject?.takeIf { currentHour == it.startHour && currentMinute == it.startMinute }?.let {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = it.name,
                                        fontSize = 10.sp,
                                        color = Color.Black,
                                    )
                                    Text(
                                        text = "${it.buildingInfo} ${it.roomInfo}",
                                        fontSize = 9.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }


        // ModalBottomSheet
        if (selectedSubject != null && showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedSubject = null
                    showBottomSheet = false
                },
                containerColor = Color.Gray
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${selectedSubject?.name} 과목을 삭제하시겠습니까?",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2, // 텍스트가 길어지면 줄바꿈을 하도록 설정
//                            overflow = androidx.compose.ui.text. , // 텍스트가 너무 길면 말줄임표 추가
                            modifier = Modifier.weight(1f) // 텍스트가 버튼과 겹치지 않도록 공간을 차지하게 함
                        )
                        Button(
                            onClick = {
                                selectedSubject?.let { onRemoveSubject(it) }
                                selectedSubject = null
                                showBottomSheet = false
                                Log.d("TimeTableManager",subjects.map{it.courseCode}.toString())
                                userViewModel.updateTimeTable(subjects.map{it.courseCode})

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("삭제", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

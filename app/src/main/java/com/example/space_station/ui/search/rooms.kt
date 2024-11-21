package com.example.space_station.ui.search

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.core.NotificationService
import com.example.space_station.core.scheduleNotification
import com.example.space_station.ui.theme.CardColors
import com.example.space_station.ui.theme.Primary
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.viewmodel.TimeTableModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rooms(
    lectureTimetable: LectureTimetable,
    timeTableModel: TimeTableModel,
    notificationService: NotificationService,
    backNavigator: () -> Unit = {},
) {
    val building = lectureTimetable.selectedBuilding.value
    val floor = lectureTimetable.selectedFloor.value
    val rooms = lectureTimetable.selectedFloorRooms.value.sortedBy { it.second }
    val userRoomsByFloor = lectureTimetable.selectedFloorUsedRooms.value.sortedBy { it[12] }

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
                            tint = Color.White,
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White,   // 제목 텍스트 색상
                    actionIconContentColor = Color.White // 액션 아이콘 색상
                ),
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
            val myNextLectureTime: String? = timeTableModel.getNextSubjectStartTime()

            rooms.forEach { (floor, item) ->
                val state = userRoomsByFloor.filter { it[12] == item }.isEmpty()
                val professor = userRoomsByFloor.filter { it[12] == item }.map { it[6] }.firstOrNull() ?: ""
                val lecture = userRoomsByFloor.filter { it[12] == item }.map { it[4] }.firstOrNull() ?: ""
                val lectureEndTime = userRoomsByFloor.filter { it[12] == item }.map { it[10] }.firstOrNull() ?: ""

                item {
                    val nextLectureTime = lectureTimetable.getNextLectureTime(
                        building,
                        floor,
                        item,
                        lectureTimetable.getNowKoreanTime(),
                        lectureTimetable.getNowKoreanDayOfWeek()
                    )

                    RoomCard(
                        room = item ,
                        onClick = {},
                        state = state,
                        professor = professor,
                        lecture = lecture,
                        lectureEndTime = lectureEndTime,
                        checkIn = { context: Context ->
                            val nextTime = nextLectureTime
                            val myTime = myNextLectureTime

                            val (message, time: String) = if (nextTime != null && myTime != null) {
                                // 둘다 널이 아닌 경우 둘중 빠른 시간에 맞춰서 푸시 알림 보내기
                                if (myTime <= nextTime) {
                                    Pair("내 다음 수업 시작 10분전 입니다. $building ${item}에서 퇴실 합니다.", myTime)
                                } else {
                                    Pair("10분 뒤에 $building ${item}에서 수업이 시작됩니다. 퇴실해 주세요.", nextTime)
                                }
                            } else if (nextTime == null && myTime != null) {
                                // 강의실의 다음 수업이 없으면 내 수업 시간에 맞춰서 푸시 알림 보내기
                                Pair("내 다음 수업 시작 10분전 입니다. $building ${item}에서 퇴실 합니다.", myTime)
                            } else if (myTime == null && nextTime != null) {
                                // 내 다음 수업이 없으면 강의실의 다음 수업에 맞춰서 푸시 알리 보내기
                                Pair("10분 뒤에 $building ${item}에서 수업이 시작됩니다. 퇴실해 주세요.", nextTime)
                            } else {
                                // 강의실의 다음 수업도 없고 내 다음 수업도 없으면 그냥 자유 입실 푸시 같은거 없음
                                // 어짜피 버튼 클릭이 불가능 함
                                Pair("", "")
                            }

                            val pushDelay = lectureTimetable.getMinuteDifference(time)
                            if ( pushDelay > 10 ){                            //체크인 함
                                lectureTimetable.CheckInRoom(building, floor, item, time)

                                // 퇴실 시간 계산해서 푸시 워커에 등록 하는 로직 만들어야 함
                                val workerID = scheduleNotification(
                                    context = context,
                                    title = "퇴실 알림",
                                    content = message,
                                    delayInMinutes = pushDelay - 10 //10분전에 알려줌
                                )
                                lectureTimetable.observeWorkCompletion(workerID.id, context)
                            } else {
                                notificationService.showBasicNotification("알림", "10분 이내에 수업이 시작 되어 입실 불가능 합니다.")
                            }
                        },
                        nextLectureTime = nextLectureTime,
                        nextMyLectureTime = myNextLectureTime,
                        isUsing = { lectureTimetable.CheckInCheck(building, floor, item) },
                        checkOut = { context: Context -> lectureTimetable.CheckOutRoom(context) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: String,
    state: Boolean = false,
    professor: String = "",
    lecture: String = "",
    lectureEndTime: String = "",
    checkIn: (Context) -> Unit = {},
    onClick: () -> Unit = {},
    nextLectureTime : String?,
    nextMyLectureTime : String?,
    isUsing: () -> Boolean = { false },
    checkOut: (Context) -> Unit = {}
) {
    var context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize(),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = CardColors // 카드 컨테이너 색상
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ){
                if (state) {
                    Column {
                        Text(room, style = typography.titleLarge)
                    }
                } else {
                    Text(room, style = typography.titleLarge)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                if (state) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("빈 강의실", style = typography.titleMedium)
                        if (nextLectureTime == null) {
                            Text("다음 수업 없음", fontSize = 12.sp)
                        } else {
                            Text("다음 수업 $nextLectureTime", fontSize = 12.sp)
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("수업 중", style = typography.titleMedium)
                        Text(lectureEndTime,  fontSize = 12.sp)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                if (state) {
                    if (nextLectureTime == null && nextMyLectureTime == null) {
                        // 강의실의 다음 수업도 없도 내 다음 수없도 없는 경우 자유 입실
                        Button(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("자유입실")
                        }
                    } else if(isUsing()) {
                        Button(
                            onClick = { checkOut(context) },
                        ) {
                            Text("퇴실하기")
                        }
                    } else {
                        Button(
                            onClick = { checkIn(context) },
                        ) {
                            Text("입실하기")
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(if (lecture.length >5) lecture.substring(0,5) + "..." else lecture, style = typography.titleMedium)
                        Text(professor,  fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
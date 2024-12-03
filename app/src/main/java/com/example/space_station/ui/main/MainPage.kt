package com.example.space_station.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.space_station.core.NotificationService
import com.example.space_station.core.scheduleNotification
import com.example.space_station.ui.layout.BottomBarComponent
import com.example.space_station.ui.layout.TopBarComponent
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.viewmodel.TimeTableModel
import com.example.space_station.viewmodel.recommendLecture

@Composable
fun MainPage(
    timeTableModel: TimeTableModel,
    lectureTimetable: LectureTimetable,
    recommendViewModel: recommendLecture,
    currentPage : Int,
    onClick: (x:Int)->Unit,
    onSettingClick:()->Unit,
    notificationService : NotificationService
) {
    val checkedInRoom = lectureTimetable.checkedInRooms.value
    var context = LocalContext.current
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "우주 정거장",
                onSettingsClick = onSettingClick
            )
        },
        bottomBar = {
            BottomBarComponent(
                currentPage = currentPage,
                onClick = onClick
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),

                ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val timeTable = timeTableModel.getPresentSubject()
                    Log.d("present Subject",timeTable.toString())
                    Log.d("present ClassRoon",checkedInRoom.toString())
                    if (timeTable != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "현재 강의실: ${timeTable.buildingInfo} ${timeTable.roomInfo}",
                                color = Color.White
                            )
                            Spacer(
                                modifier = Modifier.size(20.dp)
                            )

                        }
                    }else if(checkedInRoom != null){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "현재 강의실: ${checkedInRoom.building} ${checkedInRoom.room}",
                                color = Color.White
                            )
                            Spacer(
                                modifier = Modifier.size(20.dp)
                            )
                            Button(
                                modifier = Modifier.weight(1.0f),

                                onClick = {
                                    lectureTimetable.CheckOutRoom(context)
                                    // 퇴실하기 동작 추가
                                    println("퇴실하기 클릭됨")
                                },

                                ) {
                                Text("퇴실하기")
                            }
                        }
                    }
                    else {
                        Text(
                            "공강시간입니다.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }


                }


            }
            // Recommendation Cards
            if(checkedInRoom== null){
                val NextSubject = recommendViewModel.getNextSubject(
                    Subject = timeTableModel.subjects.value
                )

                NextSubject?.let {

                    val ClosestAvailableRoom = recommendViewModel.getClosestAvailableRoom(
                        it.buildingInfo,
                        it.roomInfo,
                        getUsedRooms = lectureTimetable::getUsedRooms,
                        getAllRoomsByBuilding = lectureTimetable::getAllRoomsByBuilding
                    )

                    ClosestAvailableRoom?.let {
                        val myNextLectureTime: String? = timeTableModel.getNextSubjectStartTime()
                        val nextLectureTime = lectureTimetable.getNextLectureTime(
                            NextSubject.buildingInfo,
                            recommendViewModel.extractionFloor(ClosestAvailableRoom.second),
                            ClosestAvailableRoom.second,
                            lectureTimetable.getNowKoreanTime(),
                            lectureTimetable.getNowKoreanDayOfWeek()
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("강의실 추천", color = Color(0xFFB2FF59))
                                    Text("${NextSubject.buildingInfo} ${ClosestAvailableRoom?.second}", color = Color.White)
                                }
                                Button(onClick = {
                                    //10분전에 알려줌
                                    //체크인 함

                                    // 퇴실 시간 계산해서 푸시 워커에 등록 하는 로직 만들어야 함
// 강의실의 다음 수업도 없고 내 다음 수업도 없으면 그냥 자유 입실 푸시 같은거 없음
                                    // 어짜피 버튼 클릭이 불가능 함
// 내 다음 수업이 없으면 강의실의 다음 수업에 맞춰서 푸시 알리 보내기
                                    // 강의실의 다음 수업이 없으면 내 수업 시간에 맞춰서 푸시 알림 보내기
                                    // 둘다 널이 아닌 경우 둘중 빠른 시간에 맞춰서 푸시 알림 보내기
                                    val nextTime = nextLectureTime
                                    val myTime = myNextLectureTime
                                    val (message, time: String) = if (nextTime != null && myTime != null) {
                                        // 둘다 널이 아닌 경우 둘중 빠른 시간에 맞춰서 푸시 알림 보내기
                                        if (myTime <= nextTime) {
                                            Pair(
                                                "내 다음 수업 시작 10분전 입니다. ${NextSubject.buildingInfo} ${ClosestAvailableRoom.second}에서 퇴실 합니다.",
                                                myTime
                                            )
                                        } else {
                                            Pair(
                                                "10분 뒤에 ${NextSubject.buildingInfo} ${ClosestAvailableRoom.second}에서 수업이 시작됩니다. 퇴실해 주세요.",
                                                nextTime
                                            )
                                        }
                                    } else if (nextTime == null && myTime != null) {
                                        // 강의실의 다음 수업이 없으면 내 수업 시간에 맞춰서 푸시 알림 보내기
                                        Pair(
                                            "내 다음 수업 시작 10분전 입니다. ${NextSubject.buildingInfo} ${ClosestAvailableRoom.second}에서 퇴실 합니다.",
                                            myTime
                                        )
                                    } else if (myTime == null && nextTime != null) {
                                        // 내 다음 수업이 없으면 강의실의 다음 수업에 맞춰서 푸시 알리 보내기
                                        Pair(
                                            "10분 뒤에 ${NextSubject.buildingInfo} ${ClosestAvailableRoom.second}에서 수업이 시작됩니다. 퇴실해 주세요.",
                                            nextTime
                                        )
                                    } else {
                                        // 강의실의 다음 수업도 없고 내 다음 수업도 없으면 그냥 자유 입실 푸시 같은거 없음
                                        // 어짜피 버튼 클릭이 불가능 함
                                        Pair("", "")
                                    }
                                    val pushDelay = lectureTimetable.getMinuteDifference(time)//10분전에 알려줌
                                    //체크인 함

                                    // 퇴실 시간 계산해서 푸시 워커에 등록 하는 로직 만들어야 함
// 강의실의 다음 수업도 없고 내 다음 수업도 없으면 그냥 자유 입실 푸시 같은거 없음
                                    // 어짜피 버튼 클릭이 불가능 함
// 내 다음 수업이 없으면 강의실의 다음 수업에 맞춰서 푸시 알리 보내기
                                    // 강의실의 다음 수업이 없으면 내 수업 시간에 맞춰서 푸시 알림 보내기
                                    // 둘다 널이 아닌 경우 둘중 빠른 시간에 맞춰서 푸시 알림 보내기
                                    if (pushDelay > 10) {                            //체크인 함
                                        lectureTimetable.CheckInRoom(
                                            NextSubject.buildingInfo,
                                            recommendViewModel.extractionFloor(
                                                ClosestAvailableRoom.second
                                            ),
                                            ClosestAvailableRoom.second,
                                            time
                                        )

                                        // 퇴실 시간 계산해서 푸시 워커에 등록 하는 로직 만들어야 함
                                        val workerID = scheduleNotification(
                                            context = context,
                                            title = "퇴실 알림",
                                            content = message,
                                            delayInMinutes = pushDelay - 10 //10분전에 알려줌
                                        )
                                        lectureTimetable.observeWorkCompletion(
                                            workerID.id,
                                            context
                                        )
                                    } else {
                                        notificationService.showBasicNotification(
                                            "알림",
                                            "10분 이내에 수업이 시작 되어 입실 불가능 합니다."
                                        )
                                    }

                                }) {
                                    Text("입실하기")
                                }
                            }
                        }

                    }

                }
            }

        }
    }

}

    

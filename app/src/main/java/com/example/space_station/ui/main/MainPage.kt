package com.example.space_station.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.space_station.core.NotificationService
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

    

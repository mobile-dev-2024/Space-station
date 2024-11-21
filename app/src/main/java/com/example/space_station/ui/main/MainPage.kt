package com.example.space_station.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.space_station.ui.layout.BottomBarComponent
import com.example.space_station.ui.layout.TopBarComponent
import com.example.space_station.viewmodel.TimeTableModel

@Composable
fun MainPage(timeTableModel: TimeTableModel, currentPage : Int, onClick: (x:Int)->Unit,onSettingClick:()->Unit) {

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
                    if (timeTable != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "현재 강의실: ${timeTable.buildingInfo}관 ${timeTable.roomInfo}호",
                                color = Color.White
                            )
                            Button(
                                onClick = {
                                    // 퇴실하기 동작 추가
                                    println("퇴실하기 클릭됨")
                                },

                            ) {
                                Text("퇴실하기")
                            }
                        }
                    } else {
                        Text(
                            "공강시간입니다.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }


                }


            }
        }
        // Recommendation Cards
        repeat(2) {
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
                        Text("310관 729호", color = Color.White)
                    }
                    Button(onClick = { /* Enter room action */ }) {
                        Text("입실하기")
                    }
                }
            }
        }
    }

}

    

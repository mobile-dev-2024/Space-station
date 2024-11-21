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

@Composable
fun MainPage(currentPage : Int, onClick: (x:Int)->Unit,onSettingClick:()->Unit){

    Scaffold(
        topBar = {
            TopBarComponent(
                title = "우주 정거장",
                onSettingsClick = onSettingClick
            )
        },
        bottomBar =  {BottomBarComponent(
            currentPage = currentPage,
            onClick = onClick
        )}

    ) {innerPadding->
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
                    Text("공강시간입니다.", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    Text("현재 강의실: 310관 726호", color = Color.White)
                    Button(
                        onClick = { /* Leave room action */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("퇴실하기")
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

    
}
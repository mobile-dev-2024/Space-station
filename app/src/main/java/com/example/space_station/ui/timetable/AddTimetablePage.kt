package com.example.space_station.ui.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.ui.theme.BackgroundColor
import com.example.space_station.ui.theme.MainContainerColor
import com.example.space_station.ui.theme.Pink80
import com.example.space_station.ui.theme.Purple80
import com.example.space_station.ui.theme.PurpleGrey80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetablePage(
    backNavigator : () -> Unit,
) {

    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("시간표 수정") },
                navigationIcon = {
                    IconButton(onClick = backNavigator) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "close"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = Color.White,
                ),
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = MainContainerColor, shape = RoundedCornerShape(0.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 검색 아이콘
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 검색 텍스트 필드
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text(
                                text = "search name",
                                color = Color.LightGray,
                                fontSize = 16.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MainContainerColor,
                            focusedContainerColor = MainContainerColor,
                            focusedIndicatorColor = MainContainerColor,
                            unfocusedIndicatorColor = MainContainerColor,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                    )
                }
            }
            items(10) {
                CourseInfoCard(
                    courseName = "모바일 앱 개발",
                    professor = "이준우 교수님",
                    roomInfo = "310관 729호",
                    schedule = "월요일 4교시, 수요일 4,5교시"
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }
        }

    }
}

@Composable
fun CourseInfoCard(
    courseName: String,
    professor: String,
    roomInfo: String,
    schedule: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = courseName,
            fontSize = 20.sp,
            color = Color.White,
        )
        Text(
            text = professor,
            fontSize = 16.sp,
            color = Color.White,
        )
        Text(
            text = roomInfo,
            fontSize = 14.sp,
            color = Color.White,
        )
        Text(
            text = schedule,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}

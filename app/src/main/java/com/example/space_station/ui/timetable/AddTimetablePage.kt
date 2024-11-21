package com.example.space_station.ui.timetable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.ui.theme.BackgroundColor
import com.example.space_station.ui.theme.MainContainerColor
import com.example.space_station.viewmodel.Lecture


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetablePage(
    backNavigator: () -> Unit,
    getLecturesBySubject: (String, String) -> List<Lecture>,
    addLectureToTimetable: (Lecture) -> Boolean // 시간표에 추가하는 함수 반환 타입을 Boolean으로 변경
) {
    var text by rememberSaveable { mutableStateOf("") }
    var lectureList by remember { mutableStateOf(listOf<Lecture>()) }
    var clickedItemIndex by remember { mutableStateOf(-1) }
    var selectedSearchType by rememberSaveable { mutableStateOf("과목명") }
    var showOverlapMessage by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() } // SnackbarHostState를 remember로 생성

    LaunchedEffect(text, selectedSearchType) {
        lectureList = getLecturesBySubject(text, selectedSearchType)
    }

    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
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

                // 검색창과 RadioButton 선택 영역
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = MainContainerColor, shape = RoundedCornerShape(0.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        listOf("과목명", "교수명", "장소명").forEach { type ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedSearchType == type,
                                    onClick = { selectedSearchType = type },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color.White,
                                        unselectedColor = Color.LightGray
                                    )
                                )
                                Text(
                                    text = type,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) } // snackbarHostState를 전달하여 SnackbarHost 초기화
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            items(lectureList.size) { index ->
                val lecture = lectureList[index]
                val isClicked = clickedItemIndex == index

                CourseInfoCard(
                    lecture = lecture,
                    isClicked = isClicked,
                    onClick = { clickedItemIndex = if (isClicked) -1 else index },
                    onAddToTimetable = {
                        val added = addLectureToTimetable(lecture)
                        if (!added) {
                            showOverlapMessage = true
                        } else {
                            clickedItemIndex = -1
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }
        }

        if (showOverlapMessage) {
            LaunchedEffect(showOverlapMessage) {
                snackbarHostState.showSnackbar("시간 겹치는 과목이 있어서, 추가가 불가능합니다")
                showOverlapMessage = false // 메시지 표시 후 상태 초기화
            }
        }
    }
}

@Composable
fun CourseInfoCard(
    lecture: Lecture,
    isClicked: Boolean,
    onClick: () -> Unit,
    onAddToTimetable: () -> Unit // 시간표에 추가하는 함수
) {
    val backgroundColor = if (isClicked) Color(0xFF421919) else BackgroundColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick,
                role = Role.Button
            )
    ) {
        Text(text = lecture.courseName, fontSize = 20.sp, color = Color.White)
        Text(text = lecture.professor, fontSize = 16.sp, color = Color.White)
        Text(text = "${lecture.buildingInfo} ${lecture.roomInfo}", fontSize = 14.sp, color = Color.White)
        Text(text = lecture.schedules.joinToString(" / ") { schedule ->
            "${schedule.day} (${schedule.time})"
        }, fontSize = 12.sp, color = Color.White)


        if (isClicked) {
            Button(
                onClick = onAddToTimetable,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("시간표에 추가")
            }
        }
    }
}

package com.example.space_station.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.space_station.ui.theme.CardColors
import com.example.space_station.ui.theme.Primary
import com.example.space_station.viewmodel.BookMarkModel
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.R
import com.example.space_station.ui.layout.BottomBarComponent
import com.example.space_station.ui.layout.TopBarComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Buildings(
    currentPage:Int,
    onClick: (Int)->Unit,
    onSettingClick:()->Unit,
    lectureTimetable: LectureTimetable,
    navigator: () -> Unit = {},
    bookMarkModel: BookMarkModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "빈 강의실 찾기",
                onSettingsClick = onSettingClick
            )
        },
        bottomBar =  {
            BottomBarComponent(
            currentPage = currentPage,
            onClick = onClick
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
            val (marked, unmarked) = lectureTimetable.buildings.value.partition {
                bookMarkModel.buildingBookMarkList[it] == true
            }

            items (marked) { buildingName ->
                BuildingCard(
                    buildingName,
                    onClick = {
                        lectureTimetable.setSelectedBuilding(buildingName)
                        navigator()
                    },
                    bookMarkToggle = { bookMarkModel.toggleBuildingBookMark(buildingName) },
                    imageId = R.drawable.marked_star
                )
            }

            items(unmarked) { buildingName ->
                BuildingCard(
                    buildingName,
                    onClick = {
                        lectureTimetable.setSelectedBuilding(buildingName)
                        navigator()
                    },
                    bookMarkToggle = { bookMarkModel.toggleBuildingBookMark(buildingName) },
                    imageId = R.drawable.unmarked_star
                )
            }
        }
    }
}

@Composable
private fun BuildingCard(
    buildingName: String,
    onClick: () -> Unit = {},
    bookMarkToggle: () -> Unit,
    imageId: Int = R.drawable.unmarked_star
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize(),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = CardColors // 카드 컨테이너 색상
        ),
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ){
            Box(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(buildingName, style = typography.titleLarge)
            }

            Box(
                modifier = Modifier.padding(16.dp),
            ){
                IconButton(
                    onClick = bookMarkToggle,
                ) {
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Star Icon",
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
        }
    }
}
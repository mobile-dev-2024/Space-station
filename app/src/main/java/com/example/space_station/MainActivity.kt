package com.example.space_station

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.space_station.ui.theme.SpacestationTheme
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.ui.search.SearchMain


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpacestationTheme {
                val lectureTimetableViewModel = viewModel<LectureTimetable>()
                lectureTimetableViewModel.loadExcelData(this)
                lectureTimetableViewModel.loadExcelData2(this)

                SearchMain(
                    lectureTimetable = lectureTimetableViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, lectureTimetable: LectureTimetable) {
    LazyColumn {
        itemsIndexed(lectureTimetable.buildings.value) { i, rowData ->
            Text(text = "$i "+ rowData)
        }
    }
//    LazyColumn(modifier = modifier.fillMaxSize()) {
//        // itemsIndexed를 사용하여 데이터의 인덱스를 활용
//        itemsIndexed(lectureTimetable.data.value) { i, rowData ->
//            Text(text = "$i "+ rowData[7])
//        }
//    }
}

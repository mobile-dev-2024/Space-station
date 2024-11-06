package com.example.space_station.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HeaderFooter.time
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LectureTimetable: ViewModel() {

    var data = mutableStateOf<List<List<String>>>(emptyList())
        private set

    var buildings = mutableStateOf<List<String>>(emptyList())
        private set


    // 데이터를 비동기로 로드
    fun loadExcelData(context: Context, fileName: String = "lecturetimetable.xlsx") {
        viewModelScope.launch(Dispatchers.IO) {
            context.assets.open(fileName).use { inputStream ->
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook.getSheetAt(0)
                val newData = mutableListOf<List<String>>()

                // 첫 번째 행을 제외하고 데이터를 로드
                val columnCount = sheet.getRow(0).lastCellNum.toInt() // 첫 번째 행의 열 개수 기준으로 고정


                for (row in sheet.drop(1)) {
                    // 각 행을 고정된 열 개수로 맞춰서 빈 셀이 있으면 ""을 추가
                    val rowData = MutableList(columnCount) { index ->
                        row.getCell(index)?.toString() ?: ""
                    }
                    newData.add(rowData)
                }

                // 데이터 로드 완료 후 상태 업데이트
                data.value = newData
            }
        }
//        다빈치 캠퍼스 건물이 떠서 310관 이하로 가져가야 함
        buildings.value = data.value.mapNotNull { it.getOrNull(11) }.distinct().sorted().filter { it <= "310관" }
    }


    fun getAllRooms(buildingName: String): Map<String, List<Pair<String, String>>> {
        return data.value
            .filter { it[11] == buildingName }    // 특정 건물명과 일치하는 행 필터링
            .mapNotNull { row ->
                val roomNumber = row[12]
                val floor = roomNumber.filter { it.isDigit() }.dropLast(2) // 숫자만 남기고 마지막 두 자리 제외
                if (floor.isNotEmpty()) floor to roomNumber else null // 층 정보가 있는 경우만 반환
            }
            .distinct()
            .sortedBy { it.first }                 // 층별 정렬
            .groupBy { it.first }
    }

    fun getUsedRooms(building: String, week: String, time: String): Map<String, List<List<String>>> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val targetTime = LocalTime.parse(time, formatter)

        val filteredData = data.value.filter { row ->
            // 건물, 요일 일치 여부 확인
            row[11] == building && row[9] == week &&
                    // 수업 시간이 타겟 시간과 겹치는지 확인
                    isTimeInRange(row[10], targetTime, formatter)
        }

        return filteredData.groupBy { row ->
            val roomNumber = row[12]
            val floor = roomNumber.filter { it.isDigit() }.dropLast(2) // 층수 추출
            floor
        }
    }

    private fun isTimeInRange(timeRange: String, targetTime: LocalTime, formatter: DateTimeFormatter): Boolean {
        // 수업 시간 범위를 시작 시간과 종료 시간으로 분리
        val (start, end) = timeRange.split("~").map { it.trim() }
        val startTime = LocalTime.parse(start, formatter)
        val endTime = LocalTime.parse(end, formatter)

        // 타겟 시간이 시작 시간과 종료 시간 사이에 있는지 확인
        return targetTime.isAfter(startTime) && targetTime.isBefore(endTime)
    }
}
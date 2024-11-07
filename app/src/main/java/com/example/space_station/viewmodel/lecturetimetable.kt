package com.example.space_station.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LectureTimetable: ViewModel() {

    var data = mutableStateOf<List<List<String>>>(emptyList())
        private set

    var buildings = mutableStateOf<List<String>>(emptyList())
        private set

    var selectedBuilding = mutableStateOf<String>("")
        private set

    fun setSelectedBuilding(building: String) {
        selectedBuilding.value = building
    }

    var selectedFloor = mutableStateOf<String>("")
        private set
    var selectedFloorRooms = mutableStateOf<List<Pair<String, String>>>(emptyList())
        private set
    var selectedFloorUsedRooms = mutableStateOf<List<List<String>>>(emptyList())
        private set

    fun setSelectedFloor(floor: String, rooms: List<Pair<String, String>>, usedRooms: List<List<String>>) {
        selectedFloor.value = floor
        selectedFloorRooms.value = rooms
        selectedFloorUsedRooms.value = usedRooms
    }

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


    fun getAllRoomsByBuilding(buildingName: String): Map<String, List<Pair<String, String>>> {
        return data.value
            .filter { it[11] == buildingName } // 특정 건물명과 일치하는 행 필터링
            .mapNotNull { row ->
                val roomNumber = row[12]

                // 층수 추출 로직
                val floor = when {
                    roomNumber.startsWith("B") -> roomNumber.take(2) // 'B'로 시작하면 'B'와 다음 숫자 포함
                    roomNumber.takeWhile { it.isDigit() }.length >= 4 -> roomNumber.take(2) // 네 자리 숫자는 처음 두 자리 사용
                    roomNumber.takeWhile { it.isDigit() }.length == 3 -> roomNumber.take(1) // 세 자리 숫자는 첫 자리만 사용
                    else -> roomNumber.takeWhile { it.isDigit() } // 그 외의 숫자만 남김
                }

                if (floor.isNotEmpty()) floor to roomNumber else null
            }
            .distinct()
            .sortedWith(compareBy { it.first.toIntOrNull() ?: Int.MAX_VALUE }) // 숫자로 변환하여 정렬
            .groupByTo(LinkedHashMap()) { it.first } // LinkedHashMap으로 그룹화하여 순서 보장
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


    fun getNowKoreanDayOfWeek(): String {
        val currentDateTime = LocalDateTime.now()
        return when (currentDateTime.dayOfWeek) {
            DayOfWeek.MONDAY -> "월"
            DayOfWeek.TUESDAY -> "화"
            DayOfWeek.WEDNESDAY -> "수"
            DayOfWeek.THURSDAY -> "목"
            DayOfWeek.FRIDAY -> "금"
            DayOfWeek.SATURDAY -> "토"
            DayOfWeek.SUNDAY -> "일"
        }
    }

    fun getNowKoreanTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentDateTime.format(formatter)
    }

    fun getNextLectureTime(buildings: String, floor: String, room: String, time: String, week: String): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val targetTime = LocalTime.parse(time, formatter)

        val filteredData = data.value.filter { row ->
            row[11] == buildings &&
                    row[12] == room &&
                    row[9] == week &&
                    LocalTime.parse(row[10].substringBefore("~"), formatter).isAfter(targetTime)
        }

        // 가장 가까운 이후 시간의 강의 찾기
        val nextLecture = filteredData.minByOrNull {
            LocalTime.parse(it[10].substringBefore("~"), formatter)
        }

        // 다음 강의의 시작 시간을 반환 (HH:mm~HH:mm 형식에서 시작 시간만 반환)
        return nextLecture?.get(10)?.substringBefore("~") ?: ""
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
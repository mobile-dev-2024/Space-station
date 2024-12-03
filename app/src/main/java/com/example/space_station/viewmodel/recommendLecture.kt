package com.example.space_station.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

class recommendLecture: ViewModel() {

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

    fun extractionFloor(roomNumber: String): String {
        // 층수 추출 로직
        val floor = when {
            roomNumber.startsWith("B") -> "-${roomNumber.drop(1).take(1)}" // 'B'로 시작하면 'B'와 다음 숫자 포함
            roomNumber.takeWhile { it.isDigit() }.length >= 4 -> roomNumber.take(2) // 네 자리 숫자는 처음 두 자리 사용
            roomNumber.takeWhile { it.isDigit() }.length == 3 -> roomNumber.take(1) // 세 자리 숫자는 첫 자리만 사용
            else -> roomNumber.takeWhile { it.isDigit() } // 그 외의 숫자만 남김
        }
        return floor
    }



    fun getNextSubject(
        Subject: List<TimetableSubject>?
    ): TimetableSubject? {
        val currentDateTime = LocalDateTime.now()
        val currentDay = getNowKoreanDayOfWeek()
        val currentTime = LocalTime.of(currentDateTime.hour, currentDateTime.minute)
//        val currentDay = "수"
//        val currentTime = LocalTime.of(12, 45)


        val todaySubjects = Subject.orEmpty().filter { it.day == currentDay }

        val nextSubject = todaySubjects
            .filter { subject ->
                val subjectStartTime = LocalTime.of(subject.startHour, subject.startMinute)
                subjectStartTime.isAfter(currentTime)
            }
            .minByOrNull { subject ->
                // 가장 가까운 시작 시간을 찾기 위해 정렬 기준 설정
                LocalTime.of(subject.startHour, subject.startMinute)
            }

        return nextSubject
    }

    fun getClosestAvailableRoom(
        targetBuilding: String,
        targetRoom: String,
        getUsedRooms: (String, String, String) -> Map<String, List<List<String>>>,
        getAllRoomsByBuilding: (String) -> Map<String, List<Pair<String, String>>>
    ):  Pair<String, String>? {

        // 입력된 강의실의 층수 추출

        val targetFloor = extractionFloor(targetRoom).toIntOrNull() ?: return null

        // 현재 요일과 시간을 가져옴
        val currentDay = getNowKoreanDayOfWeek()
        val currentTime = getNowKoreanTime()
//        val currentDay = "수"
//        val currentTime = "12:45"

        // 사용 중인 강의실 목록
        val usedRooms = getUsedRooms(targetBuilding, currentDay, currentTime)

        // 해당 건물의 모든 강의실 가져오기
        val allRoomsByFloor = getAllRoomsByBuilding(targetBuilding)

        // 사용 중이지 않은 강의실 목록을 추출
        val availableRooms = allRoomsByFloor.flatMap { (floor, rooms) ->
            rooms.filter { room -> usedRooms[floor]?.none { it[12] == room.second } == true }
        }

        // 가장 가까운 층의 강의실 찾기
        val closestRoom = availableRooms.minByOrNull { room ->
            val roomFloor = extractionFloor(room.second).toIntOrNull() ?: Int.MAX_VALUE
            kotlin.math.abs(roomFloor - targetFloor)
        }

        // 강의실 번호 반환 (없으면 null)
        return closestRoom
    }

}

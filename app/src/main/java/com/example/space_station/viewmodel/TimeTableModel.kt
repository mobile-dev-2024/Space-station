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
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeTableModel: ViewModel() {

    var data = mutableStateOf<List<List<String>>>(emptyList())
        private set

    private val _subjects = MutableLiveData<List<TimetableSubject>>()
    val subjects: LiveData<List<TimetableSubject>> = _subjects

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
        Log.d("data load from excel", "excel Load")
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


    fun getLecturesBySubject(query: String, searchType: String): List<Lecture> {
        return data.value
            .groupBy { it[3] } // courseCode로 그룹화
            .map { (_, rows) ->
                val firstRow = rows.first()
                Lecture(
                    courseCode = firstRow[3],
                    courseName = firstRow[4],
                    professor = firstRow[6],
                    buildingInfo = firstRow[11],
                    roomInfo = firstRow[12],
                    schedules = rows.map { row ->
                        Schedule(
                            day = row[9],
                            time = row[10]
                        )
                    }
                )
            }
            .filter { lecture ->
                when (searchType) {
                    "과목명" -> lecture.courseName.contains(query, ignoreCase = true)
                    "교수명" -> lecture.professor.contains(query, ignoreCase = true)
                    "장소명" -> {
                        lecture.buildingInfo.contains(query, ignoreCase = true) ||
                                lecture.roomInfo.contains(query, ignoreCase = true)
                    }
                    "코드번호" -> lecture.courseCode.contains(query, ignoreCase = true)
                    else -> false
                }
            }
    }

//    fun loadExcelData2(context: Context) {
//        // 데이터를 로드하는 로직을 구현한 뒤 subjects에 데이터를 저장
//        _subjects.value = listOf(
//            TimetableSubject("11-1", "월", 9, 30, 11, 0, "모바일 앱 개발", "100관", "100호"),
//            TimetableSubject("222", "화", 10, 0, 12, 0, "IOT 프로젝트","100관", "100호"),
//            TimetableSubject("333", "수", 13, 30, 15, 30, "ACT", "100관", "100호")
//        )
//    }

    fun splitTimeString(timeString: String): List<Int> {
        val regex = Regex("[~:]") // ~ 또는 :를 기준으로 분리
        return regex.split(timeString).mapNotNull { it.toIntOrNull() } // 각 부분을 정수로 변환, 변환 실패시 null 반환하여 제외
    }

    fun Lecture.toTimetableSubjects(): List<TimetableSubject> {
        return schedules.map { schedule ->
            val timeParts = splitTimeString(schedule.time)
            TimetableSubject(
                courseCode = this.courseCode,
                day = schedule.day,
                startHour = timeParts[0],
                startMinute = timeParts[1],
                endHour = timeParts[2],
                endMinute = timeParts[3],
                name = this.courseName,
                roomInfo = this.roomInfo,
                buildingInfo = this.buildingInfo
            )
        }
    }

    fun addSubject(lecture: Lecture): Boolean {
        val newSubjects = lecture.toTimetableSubjects()
        // 모든 새로운 강의 시간에 대해 겹치는지 확인
        val isConflicting = newSubjects.any { newSubject ->
            _subjects.value.orEmpty().any { existingSubject ->
                existingSubject.day == newSubject.day &&
                        !(newSubject.endHour < existingSubject.startHour ||
                                (newSubject.endHour == existingSubject.startHour && newSubject.endMinute <= existingSubject.startMinute) ||
                                newSubject.startHour > existingSubject.endHour ||
                                (newSubject.startHour == existingSubject.endHour && newSubject.startMinute >= existingSubject.endMinute))
            }
        }

        return if (isConflicting) {
            println("겹치는 시간이 있습니다. 다른 시간에 추가해주세요.")
            false
        } else {
            // 겹치지 않으면 모든 시간을 시간표에 추가
            val updatedList = _subjects.value.orEmpty().toMutableList()
            updatedList.addAll(newSubjects)
            _subjects.value = updatedList
            true
        }
    }

    fun removeSubject(subjectToRemove: TimetableSubject) {
        viewModelScope.launch {
            // 삭제할 과목의 이름과 동일한 모든 과목을 필터링하여 제거
            _subjects.value = _subjects.value?.filter { it.courseCode != subjectToRemove.courseCode }
        }
    }

    // 다음 수업 시작 시간을 반환
    fun getNextSubjectStartTime(): String? {
        val currentDateTime = LocalDateTime.now()
        val currentDay = getNowKoreanDayOfWeek()
        val currentTime = LocalTime.of(currentDateTime.hour, currentDateTime.minute)

        val todaySubjects = _subjects.value.orEmpty().filter { it.day == currentDay }

        val nextSubject = todaySubjects
            .filter { subject ->
                val subjectStartTime = LocalTime.of(subject.startHour, subject.startMinute)
                subjectStartTime.isAfter(currentTime)
            }
            .minByOrNull { subject ->
                // 가장 가까운 시작 시간을 찾기 위해 정렬 기준 설정
                LocalTime.of(subject.startHour, subject.startMinute)
            }

        return nextSubject?.let {
            "%02d:%02d".format(it.startHour, it.startMinute)
        }
    }

    fun getPresentSubject() : TimetableSubject? {
        val currentDateTime = LocalDateTime.now()
        val currentDay = getNowKoreanDayOfWeek()
        val currentTime = LocalTime.of(currentDateTime.hour, currentDateTime.minute)

        val todaySubjects = _subjects.value.orEmpty().filter { it.day == currentDay }
        Log.d("today Subject", todaySubjects.toString())

        val test =  todaySubjects.find { subject ->
            Log.d("Subject", subject.toString())
            val subjectStartTime = LocalTime.of(subject.startHour, subject.startMinute)
            val subjectEndTime = LocalTime.of(subject.endHour, subject.endMinute)
            Log.d("start end ", subjectStartTime.toString()+subjectEndTime.toString())
            currentTime.isAfter(subjectStartTime) && currentTime.isBefore(subjectEndTime)
        }
        Log.d("find", test.toString())
        return test
    }

    // db에 저장할 정보 return
    fun updateUserSubjectToDB() : List<String> {
        val subjectList = _subjects.value ?: return emptyList()
        return subjectList.map { it.courseCode }
    }

    // db에서 정보 받아서 사용자 시간표 로드
    fun loadUserTimeTableFromDB(courseCodeList : List<String>) {
        val updatedSubjects = mutableListOf<TimetableSubject>()

        courseCodeList.forEach { courseCode ->
            val lectures = getLecturesBySubject(courseCode, "코드번호")

            lectures.forEach { lecture ->
                updatedSubjects.addAll(lecture.toTimetableSubjects())
            }
        }
        _subjects.value = updatedSubjects
        Log.d("loadTimeTableDB", "Load TimeTable DB finished")
    }



}

// 강의 정보를 나타내는 데이터 클래스
data class Lecture(
    val courseCode: String,
    val courseName: String,
    val professor: String,
    val roomInfo: String,
    val buildingInfo: String,
    val schedules: List<Schedule> // day와 schedule을 묶어서 리스트로 관리
)

data class Schedule(
    val day: String,
    val time: String
)

data class TimetableSubject(
    val courseCode: String,
    val day: String, //월 화 와 같이 한글자
    val startHour: Int,
    val startMinute : Int,
    val endHour: Int,
    val endMinute : Int,
    val name: String,
    val roomInfo: String,
    val buildingInfo: String
)
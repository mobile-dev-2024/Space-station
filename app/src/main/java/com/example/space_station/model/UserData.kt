package com.example.space_station.model

import com.example.space_station.viewmodel.CheckedInRoom

data class UserData(
    val uid : String=""
) {
}

data class UserSettingData(
    val bookmarks:List<Int> = emptyList(),
    val isPushAvailable : Boolean = true,
    val nickname : String = "익명의 유저",
    val room : CheckedInRoom = CheckedInRoom(),
    var timetable : List<String> = emptyList(),

)

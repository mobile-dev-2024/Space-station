package com.example.space_station.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class BookMarkModel: ViewModel() {
    // 디비에 저장 해야 하는 값
    var buildingBookMarkList = mutableStateMapOf<String, Boolean>(
        "102관" to false,
        "103관" to false,
        "104관" to false,
        "105관" to false,
        "106관" to false,
        "203관" to false,
        "207관" to false,
        "208관" to false,
        "209관" to false,
        "301관" to false,
        "303관" to false,
        "304관" to false,
        "305관" to false,
        "309관" to false,
        "310관" to false,
    )
        private set

    fun toggleBuildingBookMark(building: String) {
        buildingBookMarkList[building] = !buildingBookMarkList[building]!!
        updateFireBaseFunction(buildingBookMarkList)
    }

    var updateFireBaseFunction : (Map<String, Boolean>) -> Unit = {}

    fun updateFirebaseDataToApp(bookMark: Map<String, Boolean>, updateFireBase: (Map<String, Boolean>) -> Unit) {
        for (key in bookMark.keys) {
            buildingBookMarkList[key] = bookMark[key]!!
        }
        updateFireBaseFunction = updateFireBase
    }
}
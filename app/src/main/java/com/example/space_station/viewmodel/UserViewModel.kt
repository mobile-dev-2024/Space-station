package com.example.space_station.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.space_station.firebase.FirebaseManager
import com.example.space_station.model.UserData
import com.example.space_station.model.UserSettingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel: ViewModel() {
    private var _userData = MutableStateFlow(UserData())
    var userData :StateFlow<UserData> = _userData.asStateFlow()

    private var _userSettingData = MutableStateFlow(UserSettingData())
    var userSettingData :StateFlow<UserSettingData> = _userSettingData.asStateFlow()

    fun updateUid(uid:String){
        _userData.update {
            it.copy(
                uid = uid
            )
        }
    }

    fun updateUserSettingData(userSettingData: UserSettingData){
        _userSettingData.update {
            it.copy(
                bookmarks = userSettingData.bookmarks,
                isPushAvailable = userSettingData.isPushAvailable,
                nickname = userSettingData.nickname,
                room = userSettingData.room,
                timetable = userSettingData.timetable
            )

        }
        FirebaseManager.instance.updateUserSettingData(_userData.value.uid,_userSettingData.value)

    }

    fun updatePushSetting(isPushAvailable:Boolean){
        _userSettingData.update {
            it.copy(

                isPushAvailable = isPushAvailable,

            )

        }
        FirebaseManager.instance.updateUserSettingData(_userData.value.uid,_userSettingData.value)
    }


}
package com.example.space_station.ui.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.space_station.firebase.FirebaseManager
import com.example.space_station.viewmodel.BookMarkModel
import com.example.space_station.viewmodel.LectureTimetable
import com.example.space_station.viewmodel.UserViewModel
import java.util.UUID

@Composable
fun AuthenticationManager(
    onLoginSuccess: ()->Unit,
    userViewModel: UserViewModel,
    lectureTimetable: LectureTimetable,
    bookMarkModel: BookMarkModel
) {
    var isRegisterFail by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "LoginPage"
    ) {

        composable("LoginPage") {
            LoginPage(
                onClickRegister = { navController.navigate("RegisterPage") },
                onClickLogin = {email: String, password: String ->
                    FirebaseManager.instance.signIn(email,password){
                        userViewModel.updateUid(it)
                        FirebaseManager.instance.getUserSettingData(
                            uid = it,
                            onSuccess = {
                                Log.d("LoginCheck",it.toString())
                                userViewModel.updateUserSettingData(it)
                            },
                            onError = {}
                        )
                        //로그인 하고 세팅데이터 불러 오면 렉쳐테이블 뷰모델에 데이터 업로드 함
                        val uuid = userViewModel.userSettingData.value.uuid
                        lectureTimetable.updateFirebaseDataToApp(
                            checkedInRoom = userViewModel.userSettingData.value.room,
                            uuid = if (uuid != "") {UUID.fromString(uuid)} else {null},
                            //뷰모델 안의 함수를 다른 뷰모델 안에서 호출하기 어렵기 때문에 여기서 함수를 넘겨줌
                            roomFunc = { userViewModel.updateCheckInRoom(it) },
                            uuidFunc = { userViewModel.updateCheckInRoomPushID(it) }
                        )
                        // 북마크 모델에 데이터 업로드 함
                        bookMarkModel.updateFirebaseDataToApp(
                            bookMark = userViewModel.userSettingData.value.bookmarks,
                            updateFireBase = { userViewModel.updateBookmark(it) }
                        )
                        onLoginSuccess()
                    }
                }
            )
        }
        composable("RegisterPage") {
            RegisterPage(
                isRegisterFail = isRegisterFail,
                onClickBack = { navController.navigateUp() },
                onClick = {isRegisterFail = false},
                onClickRegister = { email: String, password: String ->
                    FirebaseManager.instance.createUser(
                        email = email,
                        password = password,
                        onSuccess = {
                            FirebaseManager.instance.createUserSettingData(it)
                            userViewModel.updateUid(it)
                            onLoginSuccess()
                        },
                        onError = {
                            isRegisterFail = true
                        }

                    )
                }
            )
        }
    }
}
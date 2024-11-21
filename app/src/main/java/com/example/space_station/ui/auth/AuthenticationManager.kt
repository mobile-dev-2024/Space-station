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
import com.example.space_station.viewmodel.UserViewModel
import kotlin.script.experimental.dependencies.DependenciesResolver

@Composable
fun AuthenticationManager(
    onLoginSuccess: ()->Unit,
    userViewModel: UserViewModel,
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
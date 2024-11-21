package com.example.space_station.navigation

import SettingPage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.FirstBaseline
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.space_station.firebase.FirebaseManager
import com.example.space_station.ui.auth.AuthenticationManager
import com.example.space_station.ui.auth.LoginPage
import com.example.space_station.ui.main.LoadingPage

import com.example.space_station.ui.main.MainPage
import com.example.space_station.viewmodel.UserViewModel

@Composable
fun PageManager() {
    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    val userViewModel = viewModel<UserViewModel>()

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = if (isLoading) "LoadingPage" else if (isLoggedIn) "MainPage" else "LoginPage") {
        composable("LoadingPage") {
            LoadingPage(onClick = {
                isLoading = false
            })
        }

        composable("LoginPage") {
            AuthenticationManager(
                onLoginSuccess = { isLoggedIn = true },
                userViewModel = userViewModel,
            )
        }

        composable("MainPage") {
            MainPage(
                currentPage = currentPage,
                onClick = { x: Int -> currentPage = x },
                onSettingClick = {
                    navController.navigate("SettingPage")
                }
            )
        }

        composable("SettingPage") {
            SettingPage(
                userViewModel = userViewModel,
                onBackClick = { navController.navigateUp() },
                onLogoutClick = {
                    // 로그아웃 처리
                },
                onDeleteAccountClick = {
                    // 계정 삭제 처리
                }
            )
        }
    }
}

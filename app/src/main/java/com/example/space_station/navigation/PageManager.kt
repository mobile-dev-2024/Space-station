package com.example.space_station.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.space_station.ui.main.MainPage


@Composable
fun PageManager(){
    var currentPage by rememberSaveable{mutableStateOf(0)}
    when(currentPage){
        0 -> MainPage()
    }
}
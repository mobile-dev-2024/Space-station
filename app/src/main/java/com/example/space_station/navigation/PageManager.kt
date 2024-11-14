package com.example.space_station.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.space_station.ui.main.LoadingPage

import com.example.space_station.ui.main.MainPage


@Composable
fun PageManager(){
    var currentPage by rememberSaveable{mutableStateOf(0)}
    when(currentPage){
        0 -> LoadingPage()
        else -> MainPage(
            currentPage = currentPage,
            onClick = {x:Int->
                currentPage = x
            }

        )
    }
}
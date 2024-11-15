package com.example.space_station.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.FirstBaseline
import com.example.space_station.ui.main.LoadingPage

import com.example.space_station.ui.main.MainPage
import com.example.space_station.viewmodel.FirebaseModel


@Composable
fun PageManager(){
    var currentPage by rememberSaveable{mutableStateOf(0)}
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    val firebaseModel = FirebaseModel()

    if(isLoading){
        LoadingPage(onClick = {
            isLoading = false
        })
    }else{
        isLoggedIn = if(firebaseModel.getCurrentUser()==null) false else true

        when(currentPage){

            else -> MainPage(
                currentPage = currentPage,
                onClick = {x:Int->
                    currentPage = x
                }

            )
        }
    }

}
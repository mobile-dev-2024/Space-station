package com.example.space_station.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.example.space_station.ui.layout.BottomBarComponent
import com.example.space_station.ui.layout.TopBarComponent

@Composable
fun MainPage(currentPage : Int, onClick: (x:Int)->Unit,onSettingClick:()->Unit){

    Scaffold(
        topBar = {
            TopBarComponent(
                onSettingsClick = onSettingClick
            )
        },
        bottomBar =  {BottomBarComponent(
            currentPage = currentPage,
            onClick = onClick
        )}

    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }

    }
}
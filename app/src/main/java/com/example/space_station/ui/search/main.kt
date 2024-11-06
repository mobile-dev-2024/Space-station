package com.example.space_station.ui.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.space_station.Greeting
import com.example.space_station.ui.theme.SpacestationTheme

//서치페이지의 메인 페이지


@Composable
fun SearchMain() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable(route = "search") {

        }
        composable(route = "buildings") {

        }
        composable(route = "floors") {

        }
        composable(route = "rooms") {

        }
    }


}










@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    SpacestationTheme {
        SearchMain()
    }
}
package com.example.space_station.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarComponent(currentPage: Int, onClick: (x: Int) -> Unit) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentPadding = PaddingValues(0.dp),
        //containerColor = Color(0xFF1E1F2B),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Ensure items are spaced evenly
            ) {
                BottomBarItem(
                    icon = Icons.Filled.Search,
                    label = "검색",
                    isSelected = currentPage == 1,
                    onClick = { onClick(1) }
                )

                BottomBarItem(
                    icon = Icons.Filled.Home,
                    label = "메인",
                    isSelected = currentPage == 0,
                    onClick = { onClick(0) }
                )

                BottomBarItem(
                    icon = Icons.Filled.DateRange,
                    label = "시간표",
                    isSelected = currentPage == 2,
                    onClick = { onClick(2) }
                )
            }
        }
    )
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .requiredWidthIn(min = 100.dp)
            .clickable(onClick = onClick)
            .padding(4.dp), // External padding

        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Ensure vertical alignment
            modifier = Modifier.padding(8.dp) // Internal padding
        ) {
            Icon(imageVector = icon, contentDescription = label)
            if (isSelected) {
                Text(text = label)
            }
        }
    }
}

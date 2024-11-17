import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.space_station.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(userViewModel: UserViewModel, onBackClick: () -> Unit, onLogoutClick: () -> Unit, onDeleteAccountClick: () -> Unit) {
    var isPushNotificationEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("환경설정") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* 설정 아이콘 동작 추가 */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // 계정 섹션
            Text(
                text = "계정",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "닉네임 변경",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { /* 닉네임 변경 동작 추가 */ },
                style = LocalTextStyle.current.copy(color = Color.Gray)
            )

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            // 설정 섹션
            Text(
                text = "설정",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "푸시 알림 On/Off", color = Color.Gray)
                Switch(
                    checked = isPushNotificationEnabled,
                    onCheckedChange = { isPushNotificationEnabled = it
                        userViewModel.updatePushSetting(it)
                                      },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                )
            }

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            // 계정 섹션 (Logout and Delete account)
            Text(
                text = "계정",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "로그아웃",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onLogoutClick() },
                style = LocalTextStyle.current.copy(color = Color.Gray)
            )
            Text(
                text = "계정 탈퇴",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onDeleteAccountClick() },
                style = LocalTextStyle.current.copy(color = Color.Red)
            )
        }
    }
}

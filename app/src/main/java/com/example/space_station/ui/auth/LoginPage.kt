package com.example.space_station.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.space_station.ui.theme.Primary
import com.example.space_station.ui.theme.Secondary
import com.example.space_station.ui.theme.textFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    onClickRegister:()->Unit,
    onClickLogin:(email:String, password:String)->Unit
) {


    Scaffold(

        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary

                ),
                title ={
                    Text("Login Page", color = Color.White)
                }
            )
        }
    ) {innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
        ) {
            var id by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            Row(){
                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("id") },
                    trailingIcon = { Text("@cau.ac.kr", color = Color.Gray, modifier = Modifier.padding(end = 10.dp)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Secondary,
                        cursorColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White

                    )
                )
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Secondary,
                    cursorColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White

                )
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Button(
                onClick = { onClickLogin("${id.trim()}@cau.ac.kr",password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(5.dp)


            ) {
                Text(
                    text = "로그인",
                    style = TextStyle(
                        fontFamily = textFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        letterSpacing = 1.5.sp
                    )
                )
            }

            TextButton(
                onClick = onClickRegister,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "회원가입",
                    style = TextStyle(
                        fontFamily = textFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color.White,
                        letterSpacing = 1.5.sp
                    )
                )
            }
        }

    }
}
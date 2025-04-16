package com.example.bookapitest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.bookapitest.instances.RetrofitInstance
import com.example.bookapitest.models.UserResponse
import com.example.bookapitest.ui.theme.BookApiTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookApiTestTheme {
                val mainViewModel = MainViewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { MainTopBar(mainViewModel)}
                ) { innerPadding ->
                    var error by remember { mutableStateOf("") }
//                    var users by remember { mutableStateOf<List<UserResponse>>(emptyList()) }
//                    LaunchedEffect(Unit) {
//                        lifecycleScope.launch {
//                            try {
//                                val response = RetrofitInstance.retrofit.getUsers()
//                                if (response.isSuccessful) {
//                                    users = response.body()?: emptyList()
//                                } else {
//                                    error = "Ошибка: ${response.code()} ${response.message()}"
//                                }
//                            } catch (e: Exception) {
//                                error = "Ошибка: ${e.message}"
//                            }
//                        }
//                    }
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        Column {
                            InputField(mainViewModel)
                            BigButton(mainViewModel)
                            UserList(mainViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(mainViewModel: MainViewModel){
    val uiState = mainViewModel.uiState.collectAsState()
    OutlinedTextField(
        value = uiState.value.nameText,
        onValueChange = {mainViewModel.updateNameText(it)},
        label = {
            Row{
                Text("Псеводним", color = Color.Black)
                Text("*", color = Color.Red)
            }
        },
        supportingText = if (uiState.value.nameText.isEmpty()) {
            { Text("Название обязательно", color = Color.Red) }
        }else {
            { Text("", color = Color.Red) }
        },
        singleLine = true,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth()
    )
}

@Composable
fun UserList(mainViewModel: MainViewModel) {
    val uiState = mainViewModel.uiState.collectAsState()
    if (uiState.value.userList.isNotEmpty()){
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            itemsIndexed(uiState.value.userList){index, item->
                Row(
                    modifier = Modifier
                        .border(width = 1.dp, shape = RoundedCornerShape(0.dp), color = Color.Black)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Text(item.ID_user.toString())
                        Text(item.name)
                    }
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Закрытие",
                        modifier = Modifier.clickable {
                            mainViewModel.deleteUser(item.ID_user)
                        }
                    )
                }
            }
        }
    }
    if (uiState.value.userList.isEmpty() && uiState.value.error.isEmpty()) {
        CircularProgressIndicator()
    }
    if (uiState.value.error.isNotEmpty()) {
        Text(
            text = uiState.value.error,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BigButton(mainViewModel: MainViewModel){
    Button(
        onClick = {
            mainViewModel.createUser(mainViewModel.uiState.value.nameText)
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Добавить пользователя")
    }
}

@Composable
fun MainTopBar(mainViewModel:MainViewModel){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "ТопБар",
            color = Color.White
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.List,
            contentDescription = "Закрытие",
            tint = Color.White,
            modifier = Modifier.clickable {
                val intent = Intent(context,RecomendationsList::class.java)
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun ListUserForPreview(userList:List<UserResponse>){
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        itemsIndexed(userList){index, item->
            Row(
                modifier = Modifier
                    .border(width = 1.dp, shape = RoundedCornerShape(0.dp), color = Color.Black)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Text(item.ID_user.toString())
                    Text(item.name)
                }
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Закрытие",
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    BookApiTestTheme {
        Column {
            MainTopBar(mainViewModel = MainViewModel())
            InputField(mainViewModel = MainViewModel())
            BigButton(mainViewModel = MainViewModel())
            ListUserForPreview(listOf(
                UserResponse(0,"user1"),
                UserResponse(1,"user2")
            ))
        }
    }
}
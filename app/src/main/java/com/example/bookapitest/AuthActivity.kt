package com.example.bookapitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookapitest.ui.theme.BookApiTestTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookApiTestTheme {
                val authViewModel = AuthViewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { SimpleTopBar("Авторизация") { finish() }}
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AuthActivityScreen(authViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleInputField(
    fieldText:String,
    onFieldChange:(String)->Unit,
    label:String,
    errorMessage:String,
    isRequired:Boolean,
    ){
    OutlinedTextField(
        value = fieldText,
        onValueChange = {onFieldChange(it)},
        label = {
            Row{
                Text(label, color = Color.Black)
                if (isRequired) Text("*", color = Color.Red)
            }
        },
        supportingText = if (fieldText.isEmpty()) {
            { Text(errorMessage, color = Color.Red) }
        }else {
            { Text("", color = Color.Red) }
        },
        singleLine = true,
        modifier = Modifier.padding().fillMaxWidth()
    )
}

@Composable
fun SimpleBigButton(text:String,onClick:()->Unit){
    Button(
        onClick = {onClick()},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text,
        )
    }
}

@Composable
fun AuthActivityScreen(authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp,top = 48.dp).fillMaxSize()
    ) {
        SimpleInputField(
            uiState.emailText,
            {authViewModel.updateEmailText(it)},
            "Email",
            "Email обязателен",
            true,
        )
        SimpleInputField(
            uiState.passwordText,
            {authViewModel.updatePasswordText(it)},
            "Пароль",
            "Пароль обязателен",
            true,
        )
        SimpleBigButton(
            "Войти",
            { authViewModel.login() },
        )
        Text(uiState.tokenText)
    }
}

@Preview(showBackground = true)
@Composable
fun AuthActivityScreenPreview() {
    BookApiTestTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { SimpleTopBar("Авторизация") { }}
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding)
            ) {
                AuthActivityScreen(authViewModel = AuthViewModel())
            }
        }
    }
}
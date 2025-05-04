package com.example.bookapitest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapitest.instances.RetrofitInstance
import com.example.bookapitest.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState

    fun updateEmailText(emailText:String){
        _uiState.value = _uiState.value.copy(emailText = emailText)
    }
    fun updateUsernameText(usernameText:String){
        _uiState.value = _uiState.value.copy(usernameText = usernameText)
    }
    fun updatePasswordText(passwordText:String){
        _uiState.value = _uiState.value.copy(passwordText = passwordText)
    }
    fun updatePasswordRepeatText(passwordRepeatText:String){
        _uiState.value = _uiState.value.copy(passwordRepeatText = passwordRepeatText)
    }
    fun updateTokenText(tokenText:String){
        _uiState.value = _uiState.value.copy(tokenText = tokenText)
    }

    fun login(){
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.retrofit.login(uiState.value.emailText,uiState.value.passwordText)
                if (response.isSuccessful){
                    updateTokenText(response.body()?.access_token ?: "Токен пуст")
                }
            }catch (e: Exception){
                Log.e("Пиздец","Ну не получилось залогиниться. Хули тут поделаешь ((((((")
            }
        }
    }
}
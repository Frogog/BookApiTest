package com.example.bookapitest

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapitest.instances.RetrofitInstance
import com.example.bookapitest.models.UserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel:ViewModel(){
    private val _uiState = MutableStateFlow(MainState())
    val uiState:StateFlow<MainState> = _uiState

    fun updateNameText(nameText:String){
        _uiState.value = _uiState.value.copy(nameText = nameText)
    }

    fun createUser(nameText: String){
        viewModelScope.launch {
            try {
                RetrofitInstance.retrofit.createUser(UserRequest(nameText))
                updateUserList()
            }catch (e:Exception){
                _uiState.value = _uiState.value.copy(error = "Ошибка создания user")
            }
        }
    }

    fun deleteUser(id_user:Int){
        viewModelScope.launch {
            try {
                RetrofitInstance.retrofit.deleteUser(id_user)
                updateUserList()
            }catch (e:Exception){
                _uiState.value = _uiState.value.copy(error = "Ошибка удаления user")
            }
        }
    }

    fun updateUserList(){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.retrofit.getUsers()
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        _uiState.value = _uiState.value.copy(userList =  users)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(error =  "Ошибка: ${response.code()} ${response.message()}")
                }
            }catch (e: Exception){
                _uiState.value = _uiState.value.copy(error =  "Ошибка: ${e.message}")
            }
        }
    }

    init {
        updateUserList()
    }

}
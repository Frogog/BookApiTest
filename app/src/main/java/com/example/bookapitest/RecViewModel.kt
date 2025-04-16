package com.example.bookapitest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapitest.instances.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(RecState())
    val uiState: StateFlow<RecState> = _uiState

    fun updateIdText(idText:String){
        _uiState.value = _uiState.value.copy(id_text = idText.toInt())
    }

    fun updateRecList(id_user:Int){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.retrofit.getRecommendations(id_user)
                if (response.isSuccessful) {
                    response.body()?.let { rec ->
                        _uiState.value = _uiState.value.copy(recList =  rec)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(error =  "Ошибка: ${response.code()} ${response.message()}")
                }
            }catch (e: Exception){
                _uiState.value = _uiState.value.copy(error =  "Ошибка: ${e.message}")
            }
        }
    }
}
package com.example.bookapitest

import com.example.bookapitest.models.UserResponse

data class MainState(
    val nameText:String="",
    val error:String="",
    val userList:List<UserResponse> = listOf()
)

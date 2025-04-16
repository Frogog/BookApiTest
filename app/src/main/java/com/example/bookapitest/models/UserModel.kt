package com.example.bookapitest.models

data class UserRequest(
    val name:String
)

data class UserResponse(
    val ID_user:Int,
    val name: String
)

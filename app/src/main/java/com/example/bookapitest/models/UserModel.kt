package com.example.bookapitest.models

data class UserRequest(
    val name:String
)

data class UserResponse(
    val ID_user:Int,
    val name: String
)

data class Recommendation(
    val ISBN:String,
    val Title: String,
    val Score:Double,
    val URL:String
)
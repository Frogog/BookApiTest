package com.example.bookapitest.models

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val name:String
)

data class UserResponse(
    val ID_user:Int,
    val name: String
)

data class LoginRequest(
    @SerializedName("username")val username:String,
    @SerializedName("password")val password:String
)

data class AuthResponse(
    val access_token: String,
    val token_type:String
)

data class Recommendation(
    val ISBN:String,
    val Title: String,
    val Score:Double,
    val URL:String
)
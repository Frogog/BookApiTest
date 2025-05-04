package com.example.bookapitest

data class AuthState(
    val emailText:String="",
    val usernameText:String="",
    val passwordText:String="",
    val passwordRepeatText: String="",
    val tokenText:String="Здесь токен будет"
)

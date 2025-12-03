package com.example.myapplication.Model.Entity

data class LoginRegisterResult(
    var success: UserEntity? = null,
    var failed:String? = null
)
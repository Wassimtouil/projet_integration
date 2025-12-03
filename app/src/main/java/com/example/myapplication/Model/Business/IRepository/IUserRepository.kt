package com.example.myapplication.Model.Business.IRepository

import com.example.myapplication.Model.Entity.UserEntity

interface IUserRepository {
    suspend fun login(email:String,password:String): UserEntity?
    suspend fun register(user: UserEntity): Long
    suspend fun findUserByEmail(email:String):UserEntity?
}
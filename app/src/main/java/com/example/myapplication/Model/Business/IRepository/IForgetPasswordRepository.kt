package com.example.myapplication.Model.Business.IRepository

import com.example.myapplication.Model.Entity.UserEntity

interface IForgetPasswordRepository {
    suspend fun findUserById(userId:Int) : UserEntity
    suspend fun updateUser(user: UserEntity) : Int
}
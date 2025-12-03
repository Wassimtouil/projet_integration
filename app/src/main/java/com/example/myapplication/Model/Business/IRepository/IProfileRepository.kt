package com.example.myapplication.Model.Business.IRepository

import com.example.myapplication.Model.Entity.UserEntity

interface IProfileRepository {
    suspend fun getUserById(id:Int) : UserEntity
}
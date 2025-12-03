package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.DAO.UserDao
import com.example.myapplication.Model.Business.IRepository.IProfileRepository
import com.example.myapplication.Model.Entity.UserEntity

class ProfileRepository(val userDAo: UserDao): IProfileRepository {

    override suspend fun getUserById(id:Int) : UserEntity{
        return userDAo.findUserById(id)
    }
}
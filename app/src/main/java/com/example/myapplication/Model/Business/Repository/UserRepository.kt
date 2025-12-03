package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.DAO.UserDao
import com.example.myapplication.Model.Business.IRepository.IUserRepository
import com.example.myapplication.Model.Entity.UserEntity

class UserRepository(val userDao: UserDao): IUserRepository {
    override suspend fun login(email:String,password:String): UserEntity?{
        return userDao.login(email,password)
    }
    override suspend fun register(user: UserEntity): Long{
        return userDao.registerAccount(user)
    }

    override suspend fun findUserByEmail(email: String):UserEntity? {
        return userDao.findUserByEmail(email)
    }
}
package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.DAO.UserDao
import com.example.myapplication.Model.Business.IRepository.IEditProfileRepository
import com.example.myapplication.Model.Entity.UserEntity

class EditProfileRepository(val userDao: UserDao): IEditProfileRepository {
    override suspend fun findUserById(userId:Int): UserEntity{
        return userDao.findUserById(userId)
    }
    override suspend fun updateUser(user: UserEntity) : Int{
        return userDao.updateUserInformation(user)
    }
}
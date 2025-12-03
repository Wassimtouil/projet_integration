package com.example.myapplication.Model.Business.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.Model.Entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun registerAccount(user: UserEntity): Long

    @Query("select * from users where email= :email and password = :password")
    suspend fun login(email:String,password:String) : UserEntity?

    @Query("select * from users where email = :email")
    suspend fun findUserByEmail(email:String): UserEntity?

    @Query("select * from users where id= :id")
    suspend fun findUserById(id:Int) :UserEntity

    @Update
    suspend fun updateUserInformation(user: UserEntity): Int

}
package com.example.myapplication.Model.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.converters.Converters
import com.example.myapplication.Model.Business.DAO.TripDao
import com.example.myapplication.Model.Business.DAO.UserDao
import com.example.myapplication.Model.Entity.TripEntity
import com.example.myapplication.Model.Entity.UserEntity

@Database(entities = [UserEntity::class, TripEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun tripDao() : TripDao
}
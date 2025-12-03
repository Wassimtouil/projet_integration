package com.example.myapplication.Model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val distination:String,
    val planJson:String,
    val user_id:Int,
    val createdAt: Long= System.currentTimeMillis()
)
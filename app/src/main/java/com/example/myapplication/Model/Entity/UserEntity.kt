package com.example.myapplication.Model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var username: String,
    var email:String,
    var password:String,
    var aboutMe:String = "I love discovering new destinations and planning amazing trips.",
    var nbOfTravel:Int =0,
    var questionForgetPassword:String = "question",
    var reponseQuestion:String = "reponse"
)

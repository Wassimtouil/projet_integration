package com.example.myapplication.Model.bd

import android.content.Context
import android.content.SharedPreferences

object UserSharedPreferences{

    lateinit var p: SharedPreferences

    fun init(context: Context){
        p=context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    fun saveUserId(user_id: Int){
        p.edit().putInt("user_id",user_id).apply()
    }
    fun getUserId():Int{
        return p.getInt("user_id",-1)
    }
    fun clear(){
        p.edit().clear().apply()
    }
}
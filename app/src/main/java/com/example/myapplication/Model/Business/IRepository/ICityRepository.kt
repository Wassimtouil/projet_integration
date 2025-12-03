package com.example.myapplication.Model.Business.IRepository

interface ICityRepository {
    suspend fun getCities(prefix:String,apiKey:String): List<String>
}
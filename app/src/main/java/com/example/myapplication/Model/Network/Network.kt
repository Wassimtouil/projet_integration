package com.example.myapplication.Model.Network

import androidx.room.Room
import com.example.myapplication.Context.MyApplication
import com.example.myapplication.Model.Business.ApiService.GeminiApiService
import com.example.myapplication.Model.Business.ApiService.GeoDbApiService
import com.example.myapplication.Model.Business.ApiService.UnsplashApi
import com.example.myapplication.Model.bd.DataBase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Network {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val db: DataBase by lazy {
        Room.databaseBuilder(MyApplication.Companion.AppContext, DataBase::class.java,"travel.db").build()
    }
    val geminiRetrofit : GeminiApiService by lazy {
        Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/").client(client).
        addConverterFactory(
            GsonConverterFactory.create()).build().create(GeminiApiService::class.java)
    }
    val unsplach: UnsplashApi by lazy {
        Retrofit.Builder().baseUrl("https://api.unsplash.com/").client(client).addConverterFactory(
            GsonConverterFactory.create()).build().create(UnsplashApi::class.java)
    }
    val geoRetrofit : GeoDbApiService by lazy {
        Retrofit.Builder().baseUrl("https://wft-geo-db.p.rapidapi.com/").addConverterFactory(
            GsonConverterFactory.create()).build().create(GeoDbApiService::class.java)
    }

}
package com.example.myapplication.Context

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        lateinit var AppContext: Context
    }
    override fun onCreate() {
        super.onCreate()
        AppContext=applicationContext
    }
}
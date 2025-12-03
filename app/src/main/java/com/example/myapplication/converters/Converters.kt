package com.example.myapplication.converters

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun ListtoString(value:List<String>?):String {
        return value?.joinToString(",") ?: ""
    }
    @TypeConverter
    fun StringtoList(value: String?): List<String> {
        return value?.split(",")?.map { it.trim() } ?: emptyList()
    }
}
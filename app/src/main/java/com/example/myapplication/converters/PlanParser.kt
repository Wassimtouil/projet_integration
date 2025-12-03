package com.example.myapplication.converters

import com.example.myapplication.Model.Entity.TravelPlan
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object PlanParser {
    private val gson= Gson()
    fun extractJsonFromText(text: String): String? {
        // soustraire la première '{' jusqu'à la dernière '}' (pour récupérer le JSON)
        val start = text.indexOf("{")
        val end = text.lastIndexOf("}")
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1)
        }
        return null
    }
    fun parseTravelPlan(jsonText: String): TravelPlan? {
        return try {
            gson.fromJson(jsonText, TravelPlan::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }
}
package com.example.myapplication.Model.Business.IRepository

import com.example.myapplication.Model.Entity.TravelPlan

interface IResultRepository {
    suspend fun generatePlanGemini(prompt: String): TravelPlan
    suspend fun fetchImages(plan: TravelPlan)
    suspend fun saveTrip(plan: TravelPlan, userId: Int)
}
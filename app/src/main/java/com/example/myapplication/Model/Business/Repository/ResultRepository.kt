package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.ApiService.Content
import com.example.myapplication.Model.Business.ApiService.GeminiApiService
import com.example.myapplication.Model.Business.ApiService.GeminiRequest
import com.example.myapplication.Model.Business.ApiService.Part
import com.example.myapplication.Model.Business.ApiService.UnsplashApi
import com.example.myapplication.Model.Business.DAO.TripDao
import com.example.myapplication.Model.Business.DAO.UserDao
import com.example.myapplication.Model.Business.IRepository.IResultRepository
import com.example.myapplication.Model.Constant.API_KEY.GEMINI_KEY
import com.example.myapplication.Model.Constant.API_KEY.UNSPLASH_KEY
import com.example.myapplication.Model.Entity.TripEntity
import com.example.myapplication.converters.PlanParser
import com.example.myapplication.Model.Entity.TravelPlan
import com.google.gson.Gson

class ResultRepository(
    val tripDao: TripDao,
    val userDao: UserDao,
    val geminiApi: GeminiApiService,
    val unsplachApi: UnsplashApi
): IResultRepository {

    override suspend fun generatePlanGemini(prompt:String): TravelPlan{
        val response = geminiApi.generate(GEMINI_KEY,
            GeminiRequest(listOf(Content(listOf(Part(prompt)))))
        )
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Aucune réponse Gemini")
        val json = PlanParser.extractJsonFromText(text) ?: throw Exception("Impossible d'extraire JSON")
        return PlanParser.parseTravelPlan(json) ?: throw Exception("Impossible de parser le JSON")
    }
    override suspend fun fetchImages(plan: TravelPlan) {
        plan.days.flatMap { it.items }.forEach { item ->
            try {
                val query = item.image_query.replace("[^\\p{ASCII}]".toRegex(), "")
                val res = unsplachApi.search(query, 1, UNSPLASH_KEY)
                item.image_url = res.results.firstOrNull()?.urls?.regular ?: "https://via.placeholder.com/400x300.png?text=No+Image"
            } catch (e: Exception) {
                item.image_url = "https://via.placeholder.com/400x300.png?text=Erreur"
            }
        }
    }
    override suspend fun saveTrip(plan: TravelPlan, userId: Int) {
        val planJson = Gson().toJson(plan)
        val trip = TripEntity(distination = plan.destination, planJson = planJson, user_id = userId)
        tripDao.insertTrip(trip)
        val user=userDao.findUserById(userId)
        user.nbOfTravel++
        userDao.updateUserInformation(user)
    }
}
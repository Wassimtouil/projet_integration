package com.example.myapplication.Model.Business.ApiService

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("search/photos")
    suspend fun search(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1,
        @Query("client_id") clientId: String = "USsRFhtiuxg04WMRobngsS4Te_1ESKpvQwoI4ExtUQs" // ta clé
    ): UnsplashResponse
}
data class UnsplashResponse(val results: List<UnsplashImage>)
data class UnsplashImage(val urls: UnsplashUrls)
data class UnsplashUrls(val regular: String)

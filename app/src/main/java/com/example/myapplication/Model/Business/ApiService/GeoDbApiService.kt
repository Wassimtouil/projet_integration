package com.example.myapplication.Model.Business.ApiService

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeoDbApiService {
    @GET("v1/geo/cities")
    suspend fun getCities(
        @Query("namePrefix") prefix: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "wft-geo-db.p.rapidapi.com"
    ): Response<CityResponse>
}
data class CityResponse(val data: List<City>)
data class City(val city: String,
                val country: String,
                val countryCode: String,
                val latitude: Double,
                val longitude: Double
)

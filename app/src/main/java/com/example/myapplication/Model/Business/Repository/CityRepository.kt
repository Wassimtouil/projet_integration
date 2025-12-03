package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.ApiService.GeoDbApiService
import com.example.myapplication.Model.Business.IRepository.ICityRepository

class CityRepository(val api: GeoDbApiService): ICityRepository {
    override suspend fun getCities(prefix:String,apiKey:String): List<String>{
        val response=api.getCities(prefix,apiKey)
        if (response.isSuccessful){
            return response.body()?.data?.map { "${it.city}, ${it.country}" } ?: emptyList()
        }else {
            return emptyList()
        }
    }
}
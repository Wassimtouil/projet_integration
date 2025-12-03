package com.example.myapplication.Model.Business.IRepository

import com.example.myapplication.Model.Entity.TripEntity

interface ITripRepository {
    suspend fun getUserTrips(userId: Int): List<TripEntity>
    suspend fun insertTrip(trip: TripEntity):Long
    suspend fun getTravel(idtravel:Int): TripEntity
}
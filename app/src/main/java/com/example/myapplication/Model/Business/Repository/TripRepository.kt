package com.example.myapplication.Model.Business.Repository

import com.example.myapplication.Model.Business.DAO.TripDao
import com.example.myapplication.Model.Business.IRepository.ITripRepository
import com.example.myapplication.Model.Entity.TripEntity

class TripRepository(var tripDAO: TripDao) : ITripRepository {
    override suspend fun getUserTrips(userId: Int): List<TripEntity> {
        return tripDAO.getUserTrips(userId)
    }
    override suspend fun insertTrip(trip: TripEntity):Long{
        return tripDAO.insertTrip(trip)
    }
    override suspend fun getTravel(idtravel:Int): TripEntity{
        return tripDAO.getTravel(idtravel)
    }
}
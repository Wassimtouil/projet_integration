package com.example.myapplication.Model.Business.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Model.Entity.TripEntity

@Dao
interface TripDao {
    @Insert
    suspend fun insertTrip(trip: TripEntity): Long

    @Query("select * from trips where user_id = :userId order by createdAt desc")
    suspend fun getUserTrips(userId: Int): List<TripEntity>

    @Query("select * from trips where id = :idtravel")
    suspend fun getTravel(idtravel:Int): TripEntity

}
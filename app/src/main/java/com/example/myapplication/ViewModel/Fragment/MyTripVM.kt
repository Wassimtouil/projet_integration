package com.example.myapplication.ViewModel.Fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.ITripRepository
import com.example.myapplication.Model.Business.Repository.TripRepository
import com.example.myapplication.Model.Entity.TripEntity
import kotlinx.coroutines.launch

class MyTripVM(val repository: ITripRepository): ViewModel() {
    private val _trips = MutableLiveData<List<TripEntity>>()
    var trips: LiveData<List<TripEntity>> = _trips

    fun loadUserTrips(userId: Int){
        viewModelScope.launch {
            try {
                val tripsUser = repository.getUserTrips(userId)
                _trips.value = tripsUser
            } catch (e: Exception) {
                Log.e("RoomError", "Erreur Room : ${e.message}")
            }
        }

    }

}
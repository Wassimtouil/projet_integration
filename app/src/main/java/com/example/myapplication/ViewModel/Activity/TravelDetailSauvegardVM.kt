package com.example.myapplication.ViewModel.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.ITripRepository
import com.example.myapplication.Model.Entity.ItemWithDay
import com.example.myapplication.Model.Business.Repository.TripRepository
import com.example.myapplication.converters.PlanParser
import kotlinx.coroutines.launch

class TravelDetailSauvegardVM (val repository: ITripRepository): ViewModel(){

    private val _item= MutableLiveData<List<ItemWithDay>>()
    val item: LiveData<List<ItemWithDay>> = _item

    fun getTravel(idtravel:Int){
        viewModelScope.launch {
            val trip=repository.getTravel(idtravel)
            val plan= PlanParser.parseTravelPlan(trip.planJson)
            val liste=plan?.days?.flatMap { day ->
                day.items.map { item -> ItemWithDay(day.day, item) }
            }
            _item.value=liste
        }
    }
}
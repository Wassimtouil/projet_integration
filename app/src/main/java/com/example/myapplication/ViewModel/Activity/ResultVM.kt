package com.example.myapplication.ViewModel.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IResultRepository
import com.example.myapplication.Model.Business.Repository.ResultRepository
import com.example.myapplication.Model.Entity.TravelPlan
import kotlinx.coroutines.launch

class ResultVM(val repository: IResultRepository) : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    private val _travelPlan = MutableLiveData<TravelPlan>()
    private val _erreur = MutableLiveData<String>()
    val isLoading: LiveData<Boolean> = _isLoading
    val travelPlan: LiveData<TravelPlan> = _travelPlan
    val erreur: LiveData<String> = _erreur

    fun generatePlan(prompt:String,userId:Int){
        viewModelScope.launch {
            _isLoading.value=true
            try {
                val plan = repository.generatePlanGemini(prompt)
                repository.fetchImages(plan)

                repository.saveTrip(plan,userId)
                _travelPlan.value=plan
            }catch (e: Exception){
                _erreur.value=e.message
            }finally {
                _isLoading.value=false
            }
        }
    }
}
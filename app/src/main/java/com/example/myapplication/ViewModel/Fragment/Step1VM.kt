package com.example.myapplication.ViewModel.Fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.ICityRepository
import com.example.myapplication.Model.Business.Repository.CityRepository
import kotlinx.coroutines.launch

class Step1VM (val repository: ICityRepository): ViewModel(){

    private val _city = MutableLiveData<List<String>>()
    val city: LiveData<List<String>> = _city

    fun searchCity(prefix:String,apiKey:String){
        viewModelScope.launch {
            val resultat=repository.getCities(prefix,apiKey)
            Log.d("Step1VM", "Résultat API : $resultat")

            _city.value=resultat
        }
    }

}
package com.example.myapplication.ViewModel.Fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IProfileRepository
import com.example.myapplication.Model.Business.Repository.ProfileRepository
import com.example.myapplication.Model.Entity.UserEntity
import kotlinx.coroutines.launch

class ProfileVM (val repo: IProfileRepository): ViewModel() {
    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    fun getUserById(userId:Int){
        viewModelScope.launch {
            val u=repo.getUserById(userId)
            _user.value=u
        }
    }
}
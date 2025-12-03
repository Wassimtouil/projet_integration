package com.example.myapplication.ViewModel.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IEditProfileRepository
import com.example.myapplication.Model.Business.Repository.EditProfileRepository
import com.example.myapplication.Model.Entity.UserEntity
import kotlinx.coroutines.launch

class EditProfileVM(val repo: IEditProfileRepository): ViewModel(){

    private val _updatedUser = MutableLiveData<Boolean>()
    val updatedUser: LiveData<Boolean> = _updatedUser

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    fun loadUser(id: Int){
        viewModelScope.launch {
            val u= repo.findUserById(id)
            _user.value=u
        }
    }

    fun updateUser(id:Int,username:String,email:String,password:String,aboutme:String){
        viewModelScope.launch {
            val u= repo.findUserById(id)
            if (username.isNotEmpty())u.username=username
            if (email.isNotEmpty())u.email=email
            if (password.isNotEmpty())u.password=password
            if (aboutme.isNotEmpty()) u.aboutMe=aboutme
            if (repo.updateUser(u)==1){
                _updatedUser.value=true
                _user.value=u
            }else {
                _updatedUser.value=false
            }
        }
    }

}
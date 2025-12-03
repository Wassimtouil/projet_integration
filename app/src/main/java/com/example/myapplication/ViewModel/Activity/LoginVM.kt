package com.example.myapplication.ViewModel.Activity

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IUserRepository
import com.example.myapplication.Model.Business.Repository.UserRepository
import com.example.myapplication.Model.Entity.LoginRegisterResult
import com.example.myapplication.Model.Entity.UserEntity
import kotlinx.coroutines.launch

class LoginVM (
    val repository: IUserRepository,
    private val emailValidator: (String) -> Boolean = {Patterns.EMAIL_ADDRESS.matcher(it).matches()}
): ViewModel(){

    private val _loginResult = MutableLiveData<LoginRegisterResult>()
    val loginResult : LiveData<LoginRegisterResult> =_loginResult

    private val _forgetPassword= MutableLiveData<UserEntity>()
    val forgetPassword: LiveData<UserEntity> = _forgetPassword

    fun login(email:String,password:String){
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()){
                _loginResult.value= LoginRegisterResult(failed = "Tous les champs sont obligatoires")
                return@launch
            }
            if (!emailValidator(email)){
                _loginResult.value= LoginRegisterResult(failed = "Email est invalide")
                return@launch
            }
            if (password.length <5){
                _loginResult.value= LoginRegisterResult(failed = "Mot de passe doit etre composé de 6 lettres et chiffres")
                return@launch
            }
            val userLogin= repository.login(email,password)
            if (userLogin == null){
                _loginResult.value= LoginRegisterResult(failed = "Email ou mot de passe est incorrect")
            }else {
                _loginResult.value = LoginRegisterResult(success = userLogin)
            }
        }
    }

    fun goToForgetPassword(email:String){
        viewModelScope.launch {
            if (email.isEmpty()){
                _loginResult.value= LoginRegisterResult(failed = "Email obligatoire pour la recuperation de mot de passe")
                return@launch
            }
            if (!emailValidator(email)){
                _loginResult.value= LoginRegisterResult(failed = "Email est invalide")
                return@launch
            }
            val user=repository.findUserByEmail(email)
            if (user!=null){
                _forgetPassword.value=user
            }else {
                _loginResult.value= LoginRegisterResult(failed = "Compte n'exsite pas")
            }

        }

    }
}
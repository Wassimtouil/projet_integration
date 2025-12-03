package com.example.myapplication.ViewModel.Activity

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IUserRepository
import com.example.myapplication.Model.Business.Repository.UserRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.Model.Entity.LoginRegisterResult
import kotlinx.coroutines.launch

class RegisterVM (
    val repository: IUserRepository,
    private val emailValidator: (String) -> Boolean = {Patterns.EMAIL_ADDRESS.matcher(it).matches()}
): ViewModel(){

    private val _registerResult = MutableLiveData<LoginRegisterResult>()
    val registerResult: LiveData<LoginRegisterResult> = _registerResult

    fun register(user: UserEntity){
        viewModelScope.launch {
            if (user.username.isEmpty() || user.email.isEmpty() || user.password.isEmpty()){
                _registerResult.value= LoginRegisterResult(failed = "Tous les champs sont obligatoires")
                return@launch
            }
            if (!(user.username.all { it.isLetter() })){
                _registerResult.value= LoginRegisterResult(failed = "Username doit etre composé des lettres")
                return@launch
            }
            if (!emailValidator(user.email)){
                _registerResult.value= LoginRegisterResult(failed = "Email est incorrect")
                return@launch
            }
            if (user.password.length <5){
                _registerResult.value= LoginRegisterResult(failed = "Mot de passe doit etre de longueur >=5")
                return@launch
            }
            if (!(user.password.all { it.isLetterOrDigit() })){
                _registerResult.value= LoginRegisterResult(failed = "Mot de passe doit etre composé de lettres ou chiffres")
                return@launch
            }
            if (user.questionForgetPassword=="-- Choose an option --"){
                _registerResult.value= LoginRegisterResult(failed = "Il fait choisir un question")
                return@launch
            }
            if (user.reponseQuestion.isEmpty()){
                _registerResult.value= LoginRegisterResult(failed = "Reponse pour le question est obligatoire")
                return@launch
            }
            val userRegister=repository.register(user)
            if (userRegister>0){
                _registerResult.value= LoginRegisterResult(success = user)
            }else {
                _registerResult.value= LoginRegisterResult(failed = "Inscription n'est pas valide")
            }
        }
    }
}
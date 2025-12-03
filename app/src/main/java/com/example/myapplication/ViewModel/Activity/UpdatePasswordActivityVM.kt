package com.example.myapplication.ViewModel.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IForgetPasswordRepository
import com.example.myapplication.Model.Entity.LoginRegisterResult
import kotlinx.coroutines.launch

class UpdatePasswordActivityVM(val repository: IForgetPasswordRepository) : ViewModel() {

    private var _updated = MutableLiveData<Boolean>()
    var updated: LiveData<Boolean> = _updated

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun updateUserPassword(userId:Int,newPassword: String){
        viewModelScope.launch {
            if (newPassword.length <5){
                _message.value="Mot de passe doit etre de longueur >=5"
                return@launch
            }
            if (!(newPassword.all { it.isLetterOrDigit() })){
                _message.value= "Mot de passe doit etre composé de lettres ou chiffres"
                return@launch
            }
            val user=repository.findUserById(userId)
            user.password=newPassword
            if(repository.updateUser(user)==0){
                _updated.value=false
            }else {
                _message.value="Mot de passe modifié"
                _updated.value=true
            }
        }
    }
}
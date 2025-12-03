package com.example.myapplication.ViewModel.Activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Business.IRepository.IForgetPasswordRepository
import kotlinx.coroutines.launch

class ForgetPasswordVM(val repository: IForgetPasswordRepository) : ViewModel(){

    private var _valid= MutableLiveData<Boolean>()
    val valid: LiveData<Boolean> = _valid

    private var _question= MutableLiveData<String>()
    val QuestionValue: LiveData<String> =_question

    fun checkAnswer(userId:Int,reponse:String){
        viewModelScope.launch {
            val user= repository.findUserById(userId)
            if (user.reponseQuestion == reponse){
                _valid.value=true
            }else {
                _valid.value=false
            }

        }
    }

    fun searchQuestionOfUser(userId: Int){
        viewModelScope.launch {
            val user=repository.findUserById(userId)
            _question.value=user.questionForgetPassword
        }
    }

}
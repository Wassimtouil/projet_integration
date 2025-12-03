package com.example.myapplication.view.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Model.Business.Repository.ForgetPasswordRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.view.Activity.UpdatePasswordActivity
import com.example.myapplication.ViewModel.Activity.ForgetPasswordVM

class ForgetPasswordActivity : AppCompatActivity() {
    private var userId:Int =0
    private lateinit var ForgetPasswordViewModel : ForgetPasswordVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userId=intent.getIntExtra("userId",-1)
        val db= Network.db
        val repository= ForgetPasswordRepository(db.userDao())
        ForgetPasswordViewModel= ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ForgetPasswordVM(repository) as T
                }
            }
        )[ForgetPasswordVM::class.java]
        setupObservers()
        ForgetPasswordViewModel.searchQuestionOfUser(userId)
        setupClickButton()
    }

    private fun setupObservers(){
        ForgetPasswordViewModel.QuestionValue.observe(this){
            if (it.isNotEmpty()){
                findViewById<TextView>(R.id.question).text=it
            }
        }
        ForgetPasswordViewModel.valid.observe(this){
            if (it){
                Toast.makeText(this,"Reponse correcte", Toast.LENGTH_LONG).show()
                val i= Intent(this, UpdatePasswordActivity::class.java)
                i.putExtra("UserId",userId)
                startActivity(i)
            }else {
                Toast.makeText(this,"Reponse incorrecte", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupClickButton(){
        findViewById<Button>(R.id.btnverifier).setOnClickListener {
            ForgetPasswordViewModel.checkAnswer(userId,findViewById<EditText>(R.id.reponse).text.toString())
        }
    }
}
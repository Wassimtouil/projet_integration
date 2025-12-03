package com.example.myapplication.view.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Model.Business.Repository.UserRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.ViewModel.Activity.RegisterVM

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerViewModel: RegisterVM
    private lateinit var username : EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var spinner: Spinner
    private lateinit var response: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db= Network.db
        val repository= UserRepository(db.userDao())
        registerViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegisterVM(repository) as T
                }
            }
        )[RegisterVM::class.java]

        spinner=findViewById<Spinner>(R.id.spinner)
        val adapter= ArrayAdapter.createFromResource(this,R.array.question_array,android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter

        setupObservers()
        setupClick()
    }

    private fun setupObservers(){
        registerViewModel.registerResult.observe(this){
            it.failed?.let {
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
            it.success?.let {
                Toast.makeText(this,"Inscription valide", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
    private fun setupClick(){
        username=findViewById(R.id.username)
        email=findViewById(R.id.email)
        password= findViewById(R.id.password)
        response=findViewById(R.id.questionResponse)
        findViewById<Button>(R.id.register).setOnClickListener {
            registerViewModel.register(
                UserEntity(
                    username = username.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString(),
                    questionForgetPassword = spinner.selectedItem.toString(),
                    reponseQuestion = response.text.toString()
                )
            )
        }


    }
}
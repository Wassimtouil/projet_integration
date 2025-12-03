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
import com.example.myapplication.view.Activity.ForgetPasswordActivity
import com.example.myapplication.Model.Business.Repository.UserRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.Model.bd.UserSharedPreferences
import com.example.myapplication.ViewModel.Activity.LoginVM

class LoginActivity : AppCompatActivity() {
    private lateinit var loginviewmodel: LoginVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        UserSharedPreferences.init(this)
        if (UserSharedPreferences.getUserId()!=-1){
            startActivity(Intent(this, HomeActivity::class.java))
            return
        }
        val db= Network.db
        val repository= UserRepository(db.userDao())
        loginviewmodel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginVM(repository) as T
                }
            }
        )[LoginVM::class.java]
        setupObservers()
        setupButtonClick()
    }

    private fun setupObservers(){
        loginviewmodel.loginResult.observe(this){
            it.failed?.let {
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
            it.success?.let {
                Toast.makeText(this,"Connextion réussie", Toast.LENGTH_LONG).show()
                UserSharedPreferences.saveUserId(it.id)
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        loginviewmodel.forgetPassword.observe(this){
            if (it!=null){
                val i=Intent(this, ForgetPasswordActivity::class.java)
                i.putExtra("userId",it.id)
                startActivity(i)
            }
        }
    }
    private fun setupButtonClick(){
        val email=findViewById<EditText>(R.id.email)
        val password=findViewById<EditText>(R.id.password)
        findViewById<Button>(R.id.login).setOnClickListener {
            loginviewmodel.login(email.text.toString(),password.text.toString())
        }
        findViewById<Button>(R.id.register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        findViewById<TextView>(R.id.btnforgetpassword).setOnClickListener {
            loginviewmodel.goToForgetPassword(email.text.toString())
        }
    }
}
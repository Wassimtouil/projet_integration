package com.example.myapplication.view.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Model.Business.Repository.EditProfileRepository
import com.example.myapplication.Model.Entity.UserEntity
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.Model.bd.UserSharedPreferences
import com.example.myapplication.R
import com.example.myapplication.ViewModel.Activity.EditProfileVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {
    private lateinit var EditProfileActivityViewModel : EditProfileVM
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var aboutme: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db= Network.db
        val repository= EditProfileRepository(db.userDao())
        EditProfileActivityViewModel= ViewModelProvider(
            this,
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EditProfileVM(repository) as T
                }
            }
        )[EditProfileVM::class.java]
        username=findViewById<EditText>(R.id.username)
        email=findViewById<EditText>(R.id.email)
        password=findViewById<EditText>(R.id.password)
        aboutme=findViewById<EditText>(R.id.aboutme)
        EditProfileActivityViewModel.loadUser(UserSharedPreferences.getUserId())

        setupButtonClick()
        setupObservers()
    }

    private fun setupButtonClick(){
        findViewById<ImageView>(R.id.btngoback).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.btnsauvegarde).setOnClickListener {
            EditProfileActivityViewModel.updateUser(
                UserSharedPreferences.getUserId(),
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                aboutme.text.toString()
            )
        }

    }


    private fun setupObservers(){
        EditProfileActivityViewModel.updatedUser.observe(this){
            if (it){
                Log.d("entityupdate","${it}")
                Toast.makeText(this, "Données ont modifiées", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        EditProfileActivityViewModel.user.observe(this){
            if (it!=null){
                username.hint = it.username
                email.hint = it.email
                password.hint = it.password
                aboutme.hint= it.aboutMe
            }
        }
    }
}
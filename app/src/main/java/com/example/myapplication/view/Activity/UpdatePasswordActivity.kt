package com.example.myapplication.view.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import com.example.myapplication.ViewModel.Activity.UpdatePasswordActivityVM

class UpdatePasswordActivity : AppCompatActivity() {
    private var userId:Int = 0
    private lateinit var updatePasswordviewModel: UpdatePasswordActivityVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db= Network.db
        val repository= ForgetPasswordRepository(db.userDao())
        updatePasswordviewModel= ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UpdatePasswordActivityVM(repository) as T
                }
            }
        )[UpdatePasswordActivityVM::class.java]
        userId=intent.getIntExtra("UserId",-1)

        setupButtonClick()
        setupObservers()
    }
    private fun setupObservers(){
        updatePasswordviewModel.message.observe(this){
            if (it.isNotEmpty()){
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
        }
        updatePasswordviewModel.updated.observe(this){
            if (it==true){

                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
    private fun setupButtonClick(){
        findViewById<Button>(R.id.btnupdate).setOnClickListener {
            updatePasswordviewModel.updateUserPassword(userId,findViewById<EditText>(R.id.password).text.toString())
        }
    }
}
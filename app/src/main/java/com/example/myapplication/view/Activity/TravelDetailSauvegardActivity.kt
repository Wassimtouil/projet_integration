package com.example.myapplication.view.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.AdapterResult
import com.example.myapplication.Model.Business.Repository.TripRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.ViewModel.Activity.TravelDetailSauvegardVM

class TravelDetailSauvegardActivity : AppCompatActivity() {
    private lateinit var travelDetailSauvegardViewModel: TravelDetailSauvegardVM
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_travel_detail_sauvegard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView=findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this)
        val intentValueTravel=intent.getIntExtra("id_travel",-1)
        val db= Network.db
        val repository= TripRepository(db.tripDao())
        travelDetailSauvegardViewModel= ViewModelProvider(
            this,
            object: ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TravelDetailSauvegardVM(repository) as T
                }
            }
        )[TravelDetailSauvegardVM::class.java]
        setupObservers()
        setupButtonClick()
        travelDetailSauvegardViewModel.getTravel(intentValueTravel)
    }

    private fun setupObservers(){
        travelDetailSauvegardViewModel.item.observe(this){
            if (it.isNotEmpty()){
                recyclerView.adapter = AdapterResult(it)
            }
        }
    }
    private fun setupButtonClick(){
        findViewById<ImageView>(R.id.btngoback).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
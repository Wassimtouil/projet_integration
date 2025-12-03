package com.example.myapplication.view.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.Adapter.AdapterResult
import com.example.myapplication.Model.Business.Repository.ResultRepository
import com.example.myapplication.Model.Entity.ItemWithDay
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.Model.TravelInformationUserObject
import com.example.myapplication.R
import com.example.myapplication.Model.bd.UserSharedPreferences
import com.example.myapplication.ViewModel.Activity.ResultVM

class ResultActivity : AppCompatActivity() {
    private lateinit var ResultViewModel : ResultVM
    private lateinit var rvPlan: RecyclerView
    private lateinit var constraintLoading : ConstraintLayout
    private lateinit var constraintResult : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db= Network.db
        val repo = ResultRepository(db.tripDao(),db.userDao(), Network.geminiRetrofit, Network.unsplach)
        ResultViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ResultVM(repo) as T
                }
            }
        )[ResultVM::class.java]
        rvPlan = findViewById(R.id.recyclerview)
        constraintResult = findViewById(R.id.resultView)
        constraintLoading=findViewById<ConstraintLayout>(R.id.loadingView)
        val loadinganimation=findViewById<LottieAnimationView>(R.id.loadinganimation)
        loadinganimation.playAnimation()
        val prompt=buildPrompt(TravelInformationUserObject.pays, TravelInformationUserObject.type, TravelInformationUserObject.budget, TravelInformationUserObject.date)
        UserSharedPreferences.init(this)
        ResultViewModel.generatePlan(prompt, UserSharedPreferences.getUserId())
        setupObservers()
        setupButtonClick()
    }
    private fun setupObservers(){
        ResultViewModel.isLoading.observe(this){
            if (it)constraintLoading.visibility= View.VISIBLE
            else constraintResult.visibility= View.VISIBLE
        }
        ResultViewModel.travelPlan.observe(this){
            constraintLoading.visibility= View.GONE
            rvPlan.layoutManager = LinearLayoutManager(this)
            rvPlan.adapter =
                AdapterResult(it.days.flatMap { day -> day.items.map { ItemWithDay(day.day, it) } })
        }
        ResultViewModel.erreur.observe(this){
            if (it!=null) {
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
                Log.d("erreurvm",it)
            }
        }
    }
    private fun setupButtonClick(){
        findViewById<ImageView>(R.id.btngoback).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun buildPrompt(pays: String, type: String, budget: String, date: String):String {
        return """
            Tu es un assistant voyage. Génère un plan de voyage AU FORMAT JSON EXACT avec ce schéma :
            {
              "destination":"<ville>",
              "days":[
                {
                    "day" : 1,
                    "items" : [
                        {
                            "title": "Nom de l'activité",
                            "details" : "decription courte sur l'activité",
                            "image_query" : "mot clé sur chercher l'image",
                            "location" : "adresse ou lieu précis",
                        },
                        ...
                    ]
                }
                {
                    "day" : 2,
                    "items" : [
                        {
                            "title": "Nom de l'activité",
                            "details" : "decription courte sur l'activité",
                            "image_query" : "mot clé sur chercher l'image",
                            "location" : "adresse ou lieu précis",
                        },
                        ...
                    ]
                }
              ]
            }
            Regle importants :
             - "image_query" doit contenir un mot clé pour chercher l'image (exemple : "Tour Eiffel Paris").
             - "location" doit être une vraie adresse ou un lieu précis (ex : "Champs-Élysées, Paris").
             -  n'oublier pas l'activité de dejeuner chaque jour.
             - chaque activité doit etre avec "location" pas null.
            Destination: $pays
            Type de voyage: $type
            Budget: $budget
            Nombre de jours: $date
            Fournis uniquement le JSON (ou JSON entouré d'explications minimal si nécessaire).
        """.trimIndent()
    }

}
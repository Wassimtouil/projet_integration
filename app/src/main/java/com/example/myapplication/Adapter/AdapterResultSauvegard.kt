package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Entity.TripEntity
import com.example.myapplication.R

class AdapterResultSauvegard(val liste:List<TripEntity>, val onSeePlan: (TripEntity)-> Unit ): RecyclerView.Adapter<AdapterResultSauvegard.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.image)
        val destination: TextView = view.findViewById(R.id.destination)
        val btnseeplan: Button = view.findViewById(R.id.btnseeplan)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_plan_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip=liste[position]
        holder.destination.text=trip.distination
        holder.btnseeplan.setOnClickListener {
            onSeePlan(trip)
        }
    }

    override fun getItemCount(): Int {
        return liste.size
    }
}
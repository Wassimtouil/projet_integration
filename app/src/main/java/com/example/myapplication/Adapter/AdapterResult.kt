package com.example.myapplication.Adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Entity.ItemWithDay
import com.example.myapplication.R

class AdapterResult(val items: List<ItemWithDay>): RecyclerView.Adapter<AdapterResult.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val jour: TextView = view.findViewById(R.id.jour)
        val image: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.title)
        val details: TextView = view.findViewById(R.id.details)
        val btnlocalisation : ImageView = view.findViewById(R.id.btnMap)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_day,parent,false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        if (position==0 || data.day != items[position - 1].day){
            holder.jour.visibility = View.VISIBLE
            holder.jour.text= "Jour ${data.day}"
        }else {
            holder.jour.visibility = View.GONE
        }
        holder.title.text=data.item.title
        holder.details.text=data.item.details
        Glide.with(holder.itemView.context)
            .load(data.item.image_url)
            .centerCrop()
            .placeholder(R.drawable.bg_image_rounded)
            .into(holder.image)
        holder.btnlocalisation.setOnClickListener {
            val context = holder.itemView.context
            val query = data.item.localisation.takeIf { !it.isNullOrBlank() } ?: data.item.title
            Log.d("TravelPlan", "Localisation: ${data.item.localisation}")
            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                context.startActivity(mapIntent)
            } catch (e: Exception) {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context.startActivity(fallbackIntent)
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}
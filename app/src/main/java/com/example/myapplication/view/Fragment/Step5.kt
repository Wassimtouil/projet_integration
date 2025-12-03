package com.example.myapplication.view.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.view.Activity.ResultActivity
import com.example.myapplication.Model.TravelInformationUserObject

class Step5 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_step5, container, false)

        val destination = view.findViewById<TextView>(R.id.destination)
        val date = view.findViewById<TextView>(R.id.date)
        val type = view.findViewById<TextView>(R.id.type)
        val budget = view.findViewById<TextView>(R.id.budget)
        destination.text= TravelInformationUserObject.pays
        date.text= TravelInformationUserObject.date
        type.text= TravelInformationUserObject.type
        budget.text= TravelInformationUserObject.budget

        val MytripFragment= MytripFragment()
        val btn=view.findViewById<Button>(R.id.btngenerate)
        btn.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
        }

        return view
    }

}
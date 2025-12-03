package com.example.myapplication.view.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.example.myapplication.Model.TravelInformationUserObject

class Step4 : Fragment() {
    private lateinit var card1: CardView
    private lateinit var card2: CardView
    private lateinit var card3: CardView
    private lateinit var SelectedCard : CardView
    private var SelectedText=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_step4, container, false)

        card1=view.findViewById(R.id.card1_cheap)
        card2=view.findViewById(R.id.card2_moderate)
        card3=view.findViewById(R.id.card3_luxury)
        val listofcard=listOf(card1,card2,card3)
        listofcard.forEach { card->
            card.setOnClickListener {
                listofcard.forEach {
                    it.setBackgroundResource(R.drawable.card_normal_style)
                }
                card.setBackgroundResource(R.drawable.card_border_cliqued)
                SelectedCard = card
                val text=SelectedCard.findViewById<TextView>(R.id.cheap)
                    ?:SelectedCard.findViewById<TextView>(R.id.moderate)
                    ?:SelectedCard.findViewById<TextView>(R.id.luxury)
                SelectedText=text?.text.toString()
            }
        }

        val btn=view.findViewById<Button>(R.id.btncontinuer)
        btn.setOnClickListener {
            if (SelectedText.isNotEmpty()){
                TravelInformationUserObject.budget=SelectedText
                parentFragmentManager.beginTransaction().replace(R.id.frame_layout, Step5()).commit()
            }else {
                Toast.makeText(requireContext(),"Choisir budget", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

}
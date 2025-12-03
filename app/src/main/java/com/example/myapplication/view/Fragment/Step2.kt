package com.example.myapplication.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.Model.TravelInformationUserObject

class Step2 : Fragment() {
    private var selectedCard: CardView?=null
    private lateinit var card1: CardView
    private lateinit var card2: CardView
    private lateinit var card3: CardView
    private lateinit var card4: CardView
    lateinit var selectedText :String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_step2, container, false)
        card1=view.findViewById(R.id.card1_justme)
        card2=view.findViewById(R.id.card2_acouple)
        card3=view.findViewById(R.id.card3_family)
        card4=view.findViewById(R.id.card4_friends)
        selectedText=""
        var listofCards=listOf(card1,card2,card3,card4)
        listofCards.forEach { cardView ->
            cardView.setOnClickListener {
                listofCards.forEach {
                    it.setBackgroundResource(R.drawable.card_normal_style)
                }
                cardView.setBackgroundResource(R.drawable.card_border_cliqued)
                selectedCard=cardView
                val textView = cardView.findViewById<TextView>(R.id.justme)
                    ?: cardView.findViewById<TextView>(R.id.couple)
                    ?: cardView.findViewById<TextView>(R.id.family)
                    ?:cardView.findViewById<TextView>(R.id.friends)
                selectedText = textView?.text.toString()
            }
        }
        val btn=view.findViewById<Button>(R.id.btncontinuer)
        btn.setOnClickListener {
            if (selectedText.isNotEmpty()){
                TravelInformationUserObject.type=selectedText
                parentFragmentManager.beginTransaction().replace(R.id.frame_layout, Step3()).commit()
            }else {
                Toast.makeText(requireContext(),"Choisir type", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

}
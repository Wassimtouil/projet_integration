package com.example.myapplication.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.AdapterResultSauvegard
import com.example.myapplication.Model.Business.Repository.TripRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.view.Fragment.StepsActivity
import com.example.myapplication.view.Activity.TravelDetailSauvegardActivity
import com.example.myapplication.Model.bd.UserSharedPreferences
import com.example.myapplication.ViewModel.Fragment.MyTripVM

class MytripFragment : Fragment() {
    lateinit var constraint1: ConstraintLayout
    lateinit var constraint2: ConstraintLayout
    lateinit var recyclerView: RecyclerView
    private lateinit var MyTripFragmentViewModel : MyTripVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_mytrip, container, false)
        constraint1=view.findViewById<ConstraintLayout>(R.id.notravel)
        constraint2=view.findViewById<ConstraintLayout>(R.id.travelplan)
        recyclerView=view.findViewById<RecyclerView>(R.id.recyclerview)

        val db= Network.db
        val repo= TripRepository(db.tripDao())
        MyTripFragmentViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MyTripVM(repo) as T
                }
            }
        )[MyTripVM::class.java]
        UserSharedPreferences.init(requireContext())
        setupObservers()
        MyTripFragmentViewModel.loadUserTrips(UserSharedPreferences.getUserId())
        setupButtonClick(view)

        return view
    }
    private fun setupObservers(){

        MyTripFragmentViewModel.trips.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                constraint1.visibility= View.VISIBLE
                constraint2.visibility= View.INVISIBLE
            }else{
                constraint1.visibility= View.INVISIBLE
                constraint2.visibility= View.VISIBLE
                recyclerView.layoutManager= LinearLayoutManager(requireContext())
                recyclerView.adapter= AdapterResultSauvegard(it) { trip ->
                    val intent = Intent(requireContext(), TravelDetailSauvegardActivity::class.java)
                    intent.putExtra("id_travel", trip.id)
                    startActivity(intent)
                }
            }
        }
    }
    private fun setupButtonClick(view: View){
        view.findViewById<Button>(R.id.btnnewtrip).setOnClickListener {
            startActivity(Intent(requireContext(), StepsActivity::class.java))
        }
        view.findViewById<ImageView>(R.id.btnaddPlan).setOnClickListener {
            startActivity(Intent(requireContext(), StepsActivity::class.java))
        }
    }
}
package com.example.myapplication.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Model.Business.Repository.CityRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.Model.TravelInformationUserObject
import com.example.myapplication.ViewModel.Fragment.Step1VM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Step1 : Fragment() {
    private lateinit var Step1ViewModel : Step1VM
    private lateinit var autoCompleteTextView : AutoCompleteTextView
    val apiKey = "050c810c85mshd9fe98f55e3286ap1f45ebjsn7e7f4f0baf6d"
    private var debounceJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step1, container, false)

        val repo= CityRepository(Network.geoRetrofit)
        Step1ViewModel= ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return Step1VM(repo) as T
                }
            }
        )[Step1VM::class.java]

        autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteView)

        autoCompleteTextView.addTextChangedListener {
            debounceJob?.cancel()
            debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500) // attend 500ms après dernière frappe
                val query = it?.toString()?.trim() ?: ""
                if (query.length >= 2) { // lancer requête si >2 lettres
                    Step1ViewModel.searchCity(query,apiKey)
                }
            }
        }

        setupObservers()
        return view
    }

    private fun setupObservers(){
        Step1ViewModel.city.observe(viewLifecycleOwner){
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it
            )
            autoCompleteTextView.setAdapter(adapter)
            adapter.notifyDataSetChanged()
            autoCompleteTextView.showDropDown()
            autoCompleteTextView.setOnItemClickListener {parent,view,position,id ->
                val ville=parent.getItemAtPosition(position) as String
                TravelInformationUserObject.pays=ville
                parentFragmentManager.beginTransaction().replace(R.id.frame_layout, Step2()).commit()
            }
        }
    }
}
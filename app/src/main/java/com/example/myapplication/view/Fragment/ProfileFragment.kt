package com.example.myapplication.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.view.Activity.EditProfileActivity
import com.example.myapplication.Model.Business.Repository.ProfileRepository
import com.example.myapplication.Model.Network.Network
import com.example.myapplication.R
import com.example.myapplication.Model.bd.UserSharedPreferences
import com.example.myapplication.ViewModel.Fragment.ProfileVM
import com.example.myapplication.view.Activity.LoginActivity

class ProfileFragment : Fragment() {
    private var refrech = false
    private lateinit var ProfileViewModel : ProfileVM
    private lateinit var usernameTextView : TextView
    private lateinit var nbOfTravel: TextView
    private lateinit var aboutme: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        UserSharedPreferences.init(requireContext())
        usernameTextView=view.findViewById<TextView>(R.id.username)
        nbOfTravel=view.findViewById<TextView>(R.id.nbTravel)
        aboutme=view.findViewById<TextView>(R.id.aboutme)
        val db= Network.db
        val repository = ProfileRepository(db.userDao())
        ProfileViewModel= ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProfileVM(repository) as T
                }
            }
        )[ProfileVM::class.java]
        ProfileViewModel.getUserById(UserSharedPreferences.getUserId())
        setupObservers()
        setupButtonClick(view)
        return view
    }

    private fun setupObservers(){
        ProfileViewModel.user.observe(viewLifecycleOwner){
            if (it!=null){
                usernameTextView.text=it.username
                nbOfTravel.text=it.nbOfTravel.toString()
                aboutme.text=it.aboutMe
            }
        }
    }

    private fun setupButtonClick(view: View){
        view.findViewById<Button>(R.id.btnedit).setOnClickListener {
            refrech=true
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        view.findViewById<Button>(R.id.btnlogout).setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            UserSharedPreferences.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        if(refrech){
            ProfileViewModel.getUserById(UserSharedPreferences.getUserId())
            refrech=false
        }
    }
}
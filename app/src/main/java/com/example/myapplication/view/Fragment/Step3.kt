package com.example.myapplication.view.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.Model.TravelInformationUserObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Step3 : Fragment() {

    private var startDate: Long? = null
    private var endDate: Long? = null
    private var isSelectingStart = true
    private var nbjours : Int =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view =inflater.inflate(R.layout.fragment_step3, container, false)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val tvSelectedDates = view.findViewById<TextView>(R.id.affichedate)
        val btnContinue = view.findViewById<Button>(R.id.btncontinuer)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            val selectedDate = cal.timeInMillis

            if (isSelectingStart) {
                startDate = selectedDate
                endDate = null
                isSelectingStart = false
                tvSelectedDates.text = "Start date: ${formatter.format(Date(startDate!!))}"
            } else {
                endDate = selectedDate
                if (endDate!! < startDate!!) {
                    val temp = startDate
                    startDate = endDate
                    endDate = temp
                }
                nbjours = ((endDate!! - startDate!!) / (1000 * 60 * 60 * 24)).toInt()
                tvSelectedDates.text =
                    "From ${formatter.format(Date(startDate!!))} to ${formatter.format(Date(endDate!!))} (${nbjours} jours)"
                isSelectingStart = true
            }
        }
        btnContinue.setOnClickListener {
            if (startDate != null && endDate != null) {
                TravelInformationUserObject.date=tvSelectedDates.text.toString()
                parentFragmentManager.beginTransaction().replace(R.id.frame_layout, Step4()).commit()
            } else {
                Toast.makeText(requireContext(), "Veuillez choisir deux dates", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}
package com.example.chronolog_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewGoalsFragment : Fragment() {

    private lateinit var minGoalTextView: TextView
    private lateinit var maxGoalTextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view, container, false)

        minGoalTextView = view.findViewById(R.id.textViewMinGoal)
        maxGoalTextView = view.findViewById(R.id.textViewMaxGoal)

        database = FirebaseDatabase.getInstance().reference

        loadGoals()

        return view
    }

    private fun loadGoals() {
        database.child("dailyGoals").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val minGoal = snapshot.child("minGoal").getValue(Double::class.java) ?: 0.0
                val maxGoal = snapshot.child("maxGoal").getValue(Double::class.java) ?: 0.0

                minGoalTextView.text = "Min Goal: $minGoal"
                maxGoalTextView.text = "Max Goal: $maxGoal"
            }

            override fun onCancelled(error: DatabaseError) {
                showDialog("Failed to load goals: ${error.message}")
            }
        })
    }

    private fun showDialog(message: String) {
        // Create an AlertDialog
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK button is clicked
                dialog.dismiss()
            }
            .show()
    }
}

package com.example.chronolog_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the TextView for user_authentication
        val userAuthTextView = view.findViewById<TextView>(R.id.user_authentication)
        val language = view.findViewById<TextView>(R.id.language)
        val notification = view.findViewById<TextView>(R.id.notification)
        val data_usage = view.findViewById<TextView>(R.id.data_usage)
        val feedback = view.findViewById<TextView>(R.id.feedback)
        val permissions = view.findViewById<TextView>(R.id.permissions)
        val logout = view.findViewById<TextView>(R.id.logout)
        val delete = view.findViewById<TextView>(R.id.delete)

        // Set click listener to show dialog when TextView is clicked
        userAuthTextView.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        language.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        notification.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        data_usage.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        feedback.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        permissions.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        logout.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }

        delete.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }
    }

    private fun showDialog(message: String) {
        // Create an AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK button is clicked
                dialog.dismiss()
            }
            .show()
    }
}

package com.example.chronolog_app

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateTaskActivity: AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var etMinHours: EditText
    private lateinit var etMaxHours: EditText
    private lateinit var etEstimatedHours: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSaveTask: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().reference

        calendarView = findViewById(R.id.calendarView)
        etMinHours = findViewById(R.id.etMinHours)
        etMaxHours = findViewById(R.id.etMaxHours)
        etEstimatedHours = findViewById(R.id.etEstimatedHours)
        etDescription = findViewById(R.id.etDescription)
        btnSaveTask = findViewById(R.id.btnSaveTask)

        btnSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val taskDate = calendarView.date
        val minHours = etMinHours.text.toString().toDoubleOrNull() ?: 0.0
        val maxHours = etMaxHours.text.toString().toDoubleOrNull() ?: 0.0
        val estimatedHours = etEstimatedHours.text.toString().toDoubleOrNull() ?: 0.0
        val description = etDescription.text.toString()

        // Create a unique ID for each task
        val taskId = database.child("tasks").push().key ?: return

        val taskData = mapOf(
            "taskDate" to taskDate,
            "minHours" to minHours,
            "maxHours" to maxHours,
            "estimatedHours" to estimatedHours,
            "description" to description
        )

        database.child("tasks").child(taskId).setValue(taskData)
            .addOnSuccessListener {
                // Data saved successfully
                showDialog("Task saved successfully.")
            }
            .addOnFailureListener { e ->
                // Failed to save data
                showDialog("Failed to save task: ${e.message}")
            }
    }

    private fun showDialog(message: String) {
        // Create an AlertDialog
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK button is clicked
                dialog.dismiss()
            }
            .show()
    }
}

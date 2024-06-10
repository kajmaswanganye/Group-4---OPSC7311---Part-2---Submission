package com.example.chronolog_app

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class EditTaskActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var etMinHours: EditText
    private lateinit var etMaxHours: EditText
    private lateinit var etEstimatedHours: EditText
    private lateinit var etDescription: EditText
    private lateinit var database: DatabaseReference

    private var taskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        calendarView = findViewById(R.id.calendarView)
        etMinHours = findViewById(R.id.etMinHours)
        etMaxHours = findViewById(R.id.etMaxHours)
        etEstimatedHours = findViewById(R.id.etEstimatedHours)
        etDescription = findViewById(R.id.etDescription)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Retrieve taskId from intent extras
        taskId = intent.getIntExtra("taskId", 0)

        // Retrieve saved task data from Firebase Database using taskId
        loadTaskData(taskId)
    }

    private fun loadTaskData(taskId: Int) {
        database.child("tasks").child(taskId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val taskDate = dataSnapshot.child("taskDate").getValue(Long::class.java) ?: 0L
                val minHours = dataSnapshot.child("minHours").getValue(Float::class.java) ?: 0f
                val maxHours = dataSnapshot.child("maxHours").getValue(Float::class.java) ?: 0f
                val estimatedHours = dataSnapshot.child("estimatedHours").getValue(Float::class.java) ?: 0f
                val description = dataSnapshot.child("description").getValue(String::class.java) ?: ""

                // Set retrieved task data to the respective views
                calendarView.date = taskDate
                etMinHours.setText(minHours.toString())
                etMaxHours.setText(maxHours.toString())
                etEstimatedHours.setText(estimatedHours.toString())
                etDescription.setText(description)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EditTaskActivity, "Failed to load task data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun saveTaskData(view: View) {
        val taskDate = calendarView.date
        val minHours = etMinHours.text.toString().toFloatOrNull() ?: 0f
        val maxHours = etMaxHours.text.toString().toFloatOrNull() ?: 0f
        val estimatedHours = etEstimatedHours.text.toString().toFloatOrNull() ?: 0f
        val description = etDescription.text.toString()

        val taskData = mapOf(
            "taskDate" to taskDate,
            "minHours" to minHours,
            "maxHours" to maxHours,
            "estimatedHours" to estimatedHours,
            "description" to description
        )

        database.child("tasks").child(taskId.toString()).updateChildren(taskData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

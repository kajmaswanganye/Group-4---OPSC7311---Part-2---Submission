package com.example.chronolog_app

import android.content.Context
import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditTaskActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var etMinHours: EditText
    private lateinit var etMaxHours: EditText
    private lateinit var etEstimatedHours: EditText
    private lateinit var etDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        calendarView = findViewById(R.id.calendarView)
        etMinHours = findViewById(R.id.etMinHours)
        etMaxHours = findViewById(R.id.etMaxHours)
        etEstimatedHours = findViewById(R.id.etEstimatedHours)
        etDescription = findViewById(R.id.etDescription)

        val taskId = intent.getIntExtra("taskId", 0) // Retrieve taskId from intent extras

        // Retrieve saved task data from SharedPreferences using taskId
        val sharedPreferences = getSharedPreferences("TaskData", Context.MODE_PRIVATE)
        val taskDate = sharedPreferences.getLong("taskDate_$taskId", 0)
        val minHours = sharedPreferences.getFloat("minHours_$taskId", 0f)
        val maxHours = sharedPreferences.getFloat("maxHours_$taskId", 0f)
        val estimatedHours = sharedPreferences.getFloat("estimatedHours_$taskId", 0f)
        val description = sharedPreferences.getString("description_$taskId", "")

        // Set retrieved task data to the respective views
        calendarView.date = taskDate
        etMinHours.setText(minHours.toString())
        etMaxHours.setText(maxHours.toString())
        etEstimatedHours.setText(estimatedHours.toString())
        etDescription.setText(description)
    }
}

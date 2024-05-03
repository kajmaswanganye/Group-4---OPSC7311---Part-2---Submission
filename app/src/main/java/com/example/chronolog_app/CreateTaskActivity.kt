package com.example.chronolog_app
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateTaskActivity: AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var etMinHours: EditText
    private lateinit var etMaxHours: EditText
    private lateinit var etEstimatedHours: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSaveTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)


        val taskId = intent.getIntExtra("taskId", 0) // Retrieve taskId from intent extras
        calendarView = findViewById(R.id.calendarView)
        etMinHours = findViewById(R.id.etMinHours)
        etMaxHours = findViewById(R.id.etMaxHours)
        etEstimatedHours = findViewById(R.id.etEstimatedHours)
        etDescription = findViewById(R.id.etDescription)
        btnSaveTask = findViewById(R.id.btnSaveTask)


        btnSaveTask.setOnClickListener {
            saveTask(taskId) // Pass taskId to saveTask method
        }
    }

    private fun saveTask(taskId: Int) { // Receive taskId as a parameter
        val sharedPreferences = getSharedPreferences("TaskData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val taskDate = calendarView.date
        val minHours = etMinHours.text.toString().toDoubleOrNull() ?: 0.0
        val maxHours = etMaxHours.text.toString().toDoubleOrNull() ?: 0.0
        val estimatedHours = etEstimatedHours.text.toString().toDoubleOrNull() ?: 0.0
        val description = etDescription.text.toString()

        // Save task data to SharedPreferences with taskId as part of the key
        editor.putLong("taskDate_$taskId", taskDate)
        editor.putFloat("minHours_$taskId", minHours.toFloat())
        editor.putFloat("maxHours_$taskId", maxHours.toFloat())
        editor.putFloat("estimatedHours_$taskId", estimatedHours.toFloat())
        editor.putString("description_$taskId", description)
        editor.apply()
    }
}

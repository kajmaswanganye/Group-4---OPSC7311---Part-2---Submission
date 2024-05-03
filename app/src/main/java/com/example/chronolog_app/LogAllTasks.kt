package com.example.chronolog_app

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class LogAllTasks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_all_tasks)

        // Retrieve the category name from the intent extra
        val categoryName = intent.getStringExtra("categoryName")

        // Display the category name in the TextView
        findViewById<TextView>(R.id.tvLogTaskCategoryName).text = categoryName

        // Retrieve all task data for the specified category
        displayTaskData(categoryName)
    }

    private fun displayTaskData(categoryName: String?) {
        if (categoryName.isNullOrEmpty()) return

        val sharedPreferences = getSharedPreferences("ButtonData", Context.MODE_PRIVATE)
        val count = sharedPreferences.getInt("buttonCount", 0)
        val taskStringBuilder = StringBuilder()

        for (i in 1..count) {
            val taskName = sharedPreferences.getString("taskName_$i", "") ?: ""
            val taskCategory = sharedPreferences.getString("taskCategory_$i", "") ?: ""

            // Check if the task belongs to the specified category
            if (taskCategory == categoryName) {
                val taskId = sharedPreferences.getInt("taskId_$i", 0)
                val hours = sharedPreferences.getString("hours_$taskId", "")
                val taskDate = sharedPreferences.getString("date_$taskId", "")

                // Append task details to the StringBuilder
                taskStringBuilder.append("Task Name: $taskName\n")
                    .append("Hours: $hours\n")
                    .append("Date: $taskDate\n\n")
            }
        }

        // Display the task data in a TextView
        findViewById<TextView>(R.id.tvTaskDetails).text = taskStringBuilder.toString()
    }
}

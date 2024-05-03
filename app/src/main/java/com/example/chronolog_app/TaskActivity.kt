package com.example.chronolog_app

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class TaskActivity : AppCompatActivity() {

    private lateinit var parentLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private var cardViewCounter = 0
    val REQUEST_CODE_LOG_HOURS = 123



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // Move the intent extra retrieval here
        val categoryID = intent.getIntExtra("categoryID", 0)

        parentLayout = findViewById(R.id.parent_layout)
        sharedPreferences = getSharedPreferences("ButtonData", Context.MODE_PRIVATE)

        parentLayout.removeAllViewsInLayout()
        val categoryName = intent.getStringExtra("categoryName")

        // Now you can use categoryName and categoryID
        findViewById<TextView>(R.id.tvCategoryName).text = categoryName

        cardViewCounter = categoryID


        restoreButtons(categoryName)
    }


    private fun restoreButtons(categoryName: String?) {
        // Get the number of buttons previously saved
        val count = sharedPreferences.getInt("buttonCount", 0)
        for (i in 1..count) {
            val taskName = sharedPreferences.getString("taskName_$i", "") ?: ""
            val taskCategory = sharedPreferences.getString("taskCategory_$i", "") ?: ""
            if (taskName.isNotEmpty() && taskCategory == categoryName) {
                val taskId = sharedPreferences.getInt("taskId_$i", 0) // Retrieve the task's unique identifier
                createButtonSet(taskName, taskId) // Pass both task name and unique identifier
            }
        }
    }

    private fun saveButton(taskName: String, categoryName: String) {
        val count = sharedPreferences.getInt("buttonCount", 0) + 1
        val taskId = cardViewCounter++ // Assign a unique identifier to the task
        sharedPreferences.edit().apply {
            putString("taskName_$count", taskName)
            putString("taskCategory_$count", categoryName)
            putInt("taskId_$count", taskId) // Save the task's unique identifier
            putInt("buttonCount", count)
            apply()
        }
    }

    private fun deleteButton(cardViewId: Int) {
        // Remove the task name associated with the cardViewId
        sharedPreferences.edit().remove("taskName_$cardViewId").apply()
    }


    //Asks the user to give the task a name
    //before creating it
    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Task Name")
        val input = EditText(this)
        input.hint = "Task Name"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val taskName = input.text.toString()
            val sharedPreferences = getSharedPreferences("TaskName", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("TaskName", taskName)
            editor.apply()
            createButtonSet(taskName, taskId)

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOG_HOURS && resultCode == Activity.RESULT_OK) {
            val categoryName = data?.getStringExtra("categoryName")
            val updatedTotalHours = data?.getIntExtra("hours", 0)
            // Update the display with the updated total hours for the category
            // For example, find the appropriate TextView and set its text to the updated total hours
        }
    }


    //creates the physical button, and set id's to them all
    private fun createButtonSet(taskName: String, taskId:Int) {
        cardViewCounter++

        val categoryName = intent.getStringExtra("categoryName")
        if (categoryName != null) {
            saveButton(taskName, categoryName)
        }


        val cardView = CardView(this)
        cardView.id = cardViewCounter

        val cardViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardViewLayoutParams.setMargins(0, 0, 0, 16) // Add bottom margin to CardView
        cardView.layoutParams = cardViewLayoutParams
        cardView.radius = 16f
        cardView.setContentPadding(16, 16, 16, 16)

        val buttonSetLayout = LinearLayout(this)
        buttonSetLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonSetLayout.orientation = LinearLayout.VERTICAL

        // Create TextView for task name
        val textView = TextView(this)
        textView.text = "Task Name: $taskName"
        buttonSetLayout.addView(textView)

        val buttonsLayout = LinearLayout(this)
        buttonsLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonsLayout.orientation = LinearLayout.HORIZONTAL


        val createTaskButton = createButton("Create Task", cardViewCounter, taskName)
        val editTaskButton = createButton("Edit Task", cardViewCounter, taskName)
        val logHoursButton = createButton("Log Hours", cardViewCounter, taskName)
        val deleteTaskButton = createButton("Delete Task", cardViewCounter, taskName)

        buttonsLayout.addView(createTaskButton)
        buttonsLayout.addView(editTaskButton)
        buttonsLayout.addView(logHoursButton)
        buttonsLayout.addView(deleteTaskButton)

        buttonSetLayout.addView(buttonsLayout)
        cardView.addView(buttonSetLayout)

        parentLayout.addView(cardView)

    }


    //button functions and click events
    private fun createButton(text: String, cardViewId: Int, taskName: String): Button {
        val button = Button(this)
        val buttonLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.layoutParams = buttonLayoutParams
        button.text = text
        //button click events
        button.setOnClickListener {
            if (text == "Delete Task") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        removeButtonSet(cardViewId)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else if (text == "Create Task") {
                val intent = Intent(this, CreateTaskActivity::class.java)
                intent.putExtra("buttonId", cardViewId)
                intent.putExtra("TaskName", taskName)
                startActivity(intent)
            }
            else if (text == "Edit Task") {
                val intent = Intent(this, EditTaskActivity::class.java)
                intent.putExtra("buttonId", cardViewId)
                intent.putExtra("TaskName", taskName)
                startActivity(intent)
            }
            else if (text == "Log Hours") {
                val intent = Intent(this, LogHours::class.java)
                startActivity(intent)
            }
        }
        return button
    }


    //delete button code
    private fun removeButtonSet(cardViewId: Int) {

        sharedPreferences.edit().remove("taskName_$cardViewId").apply()
        val cardView = findViewById<CardView>(cardViewId)
        parentLayout.removeView(cardView)
        deleteButton(cardViewId)

    }


    fun addTask(view: View) {
        showAddTaskDialog()
    }

    fun RemoveAll(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete All Tasks")
            .setMessage("Are you sure you want to delete all tasks?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear all tasks from SharedPreferences
                sharedPreferences.edit().clear().apply()
                // Remove all task views from the layout
                parentLayout.removeAllViews()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    fun BackToCategory(view: View)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Back To Category Page")
            .setMessage("Would You Like To Go Back To Category Page?")
            .setPositiveButton("Yes") { _, _ ->
                parentLayout.removeAllViews()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

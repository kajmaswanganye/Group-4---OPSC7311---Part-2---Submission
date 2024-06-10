package com.example.chronolog_app

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TaskActivity : AppCompatActivity() {

    private lateinit var parentLayout: LinearLayout
    private lateinit var database: DatabaseReference
    private var cardViewCounter = 0
    val REQUEST_CODE_LOG_HOURS = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Move the intent extra retrieval here
        val categoryID = intent.getIntExtra("categoryID", 0)

        parentLayout = findViewById(R.id.parent_layout)

        parentLayout.removeAllViewsInLayout()
        val categoryName = intent.getStringExtra("categoryName")

        // Now you can use categoryName and categoryID
        findViewById<TextView>(R.id.tvCategoryName).text = categoryName

        cardViewCounter = categoryID

        restoreButtons(categoryName)
    }

    private fun restoreButtons(categoryName: String?) {
        database.child("tasks").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { taskSnapshot ->
                val taskName = taskSnapshot.child("taskName").getValue(String::class.java)
                val taskCategory = taskSnapshot.child("taskCategory").getValue(String::class.java)
                val taskId = taskSnapshot.child("taskId").getValue(Int::class.java) ?: 0
                if (taskName != null && taskCategory == categoryName) {
                    createButtonSet(taskName, taskId)
                }
            }
        }
    }

    private fun saveButton(taskName: String, categoryName: String, taskId: Int) {
        val taskData = hashMapOf(
            "taskName" to taskName,
            "taskCategory" to categoryName,
            "taskId" to taskId
        )
        database.child("tasks").child(taskId.toString()).setValue(taskData)
    }

    private fun deleteButton(taskId: Int) {
        database.child("tasks").child(taskId.toString()).removeValue()
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Task Name")
        val input = EditText(this)
        input.hint = "Task Name"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val taskName = input.text.toString()
            val taskId = cardViewCounter++
            saveButton(taskName, intent.getStringExtra("categoryName") ?: "", taskId)
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

    private fun createButtonSet(taskName: String, taskId: Int) {
        val cardView = CardView(this)
        cardView.id = taskId

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

        val createTaskButton = createButton("Create Task", taskId, taskName)
        val editTaskButton = createButton("Edit Task", taskId, taskName)
        val logHoursButton = createButton("Log Hours", taskId, taskName)
        val deleteTaskButton = createButton("Delete Task", taskId, taskName)

        buttonsLayout.addView(createTaskButton)
        buttonsLayout.addView(editTaskButton)
        buttonsLayout.addView(logHoursButton)
        buttonsLayout.addView(deleteTaskButton)

        buttonSetLayout.addView(buttonsLayout)
        cardView.addView(buttonSetLayout)

        parentLayout.addView(cardView)
    }

    private fun createButton(text: String, cardViewId: Int, taskName: String): Button {
        val button = Button(this)
        val buttonLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.layoutParams = buttonLayoutParams
        button.text = text
        button.setOnClickListener {
            when (text) {
                "Delete Task" -> {
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
                }
                "Create Task" -> {
                    val intent = Intent(this, CreateTaskActivity::class.java)
                    intent.putExtra("buttonId", cardViewId)
                    intent.putExtra("TaskName", taskName)
                    startActivity(intent)
                }
                "Edit Task" -> {
                    val intent = Intent(this, EditTaskActivity::class.java)
                    intent.putExtra("buttonId", cardViewId)
                    intent.putExtra("TaskName", taskName)
                    startActivity(intent)
                }
                "Log Hours" -> {
                    val intent = Intent(this, LogHours::class.java)
                    startActivity(intent)
                }
            }
        }
        return button
    }

    private fun removeButtonSet(cardViewId: Int) {
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
                database.child("tasks").removeValue()
                parentLayout.removeAllViews()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun BackToCategory(view: View) {
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

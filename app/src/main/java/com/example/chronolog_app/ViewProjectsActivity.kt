package com.example.chronolog_app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewProjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_projects)

        // Dummy data for past projects
        val projects = listOf(
            "Employee: John Doe\nTask: Design Mockups\nCompleted: 2023-04-15\nDuration: 3 days",
            "Employee: Alice Smith\nTask: Backend Development\nCompleted: 2023-05-20\nDuration: 5 days",
            "Employee: Bob Johnson\nTask: Database Setup\nCompleted: 2023-06-10\nDuration: 2 days",
            "Employee: Emma Brown\nTask: UI Implementation\nCompleted: 2023-07-05\nDuration: 4 days",
            "Employee: Michael White\nTask: Testing and QA\nCompleted: 2023-08-12\nDuration: 7 days",
            "Employee: Sarah Lee\nTask: Frontend Integration\nCompleted: 2023-09-01\nDuration: 6 days",
            "Employee: David Williams\nTask: Deployment\nCompleted: 2023-10-18\nDuration: 4 days",
            "Employee: Emily Taylor\nTask: Documentation\nCompleted: 2023-11-22\nDuration: 3 days",
            "Employee: Kevin Clark\nTask: Project Planning\nCompleted: 2023-12-10\nDuration: 5 days",
            "Employee: Jessica Hall\nTask: Client Meetings\nCompleted: 2024-01-05\nDuration: 2 days"
        )

        // Find the ListView from the layout
        val listViewProjects: ListView = findViewById(R.id.listViewProjects)

        // Create an ArrayAdapter to bind the data to the ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, projects)

        // Set the adapter to the ListView
        listViewProjects.adapter = adapter
    }
}

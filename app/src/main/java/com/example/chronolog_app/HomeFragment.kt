package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize drawerLayout and actionBarDrawerToggle
        drawerLayout = view.findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(requireActivity(), drawerLayout, R.string.nav_open, R.string.nav_close)

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Show the Navigation drawer icon on the action bar
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find ImageView for drawer open action
        val ivDrawerOpen: ImageView = view.findViewById(R.id.ivDrawerOpen)

        // Set click listener for opening the drawer
        ivDrawerOpen.setOnClickListener {
            // Open the drawer
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Find CardViews and Button in the fragment layout
        val cardViewUser: View = view.findViewById(R.id.user)
        val cardViewProjects: View = view.findViewById(R.id.view_projects)
        val cardViewGoals: View = view.findViewById(R.id.set_daily_goals)

        // Set click listeners for CardViews
        cardViewUser.setOnClickListener {
            // Navigate to View Projects activity
            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
        }

        cardViewProjects.setOnClickListener {
            // Navigate to View Projects activity
            startActivity(Intent(requireActivity(), ViewProjectsActivity::class.java))
        }

        cardViewGoals.setOnClickListener {
            // Navigate to Set Daily Goals activity
            startActivity(Intent(requireActivity(), SetDailyGoalsActivity::class.java))
        }

        // Set up RecyclerView for recent entries
        val recyclerView: RecyclerView = view.findViewById(R.id.recentEntryRView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for recent entries (Date, Category, Task)
        val recentEntries = listOf(
            Entry("2023-04-15", "Design", "Mockups"),
            Entry("2023-05-20", "Development", "Backend"),
            Entry("2023-06-10", "Database", "Setup"),
            Entry("2023-07-05", "UI", "Implementation"),
            Entry("2023-08-12", "Testing", "QA"),
            Entry("2023-09-01", "Frontend", "Integration"),
            Entry("2023-10-18", "Deployment", "Tasks"),
            Entry("2023-11-22", "Documentation", "Tasks"),
            Entry("2023-12-10", "Project Planning", "Tasks"),
            Entry("2024-01-05", "Client Meetings", "Tasks")
        )

        // Set up adapter for RecyclerView
        val adapter = RecentEntryAdapter(recentEntries)
        recyclerView.adapter = adapter

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle clicks on the ActionBarDrawerToggle
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    data class Entry(val date: String, val category: String, val task: String)
}
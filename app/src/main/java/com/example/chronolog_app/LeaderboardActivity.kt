package com.example.chronolog_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var recyclerViewLeaderboard: RecyclerView // Declare RecyclerView variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Initialize recyclerViewLeaderboard
        recyclerViewLeaderboard = findViewById(R.id.recyclerViewLeaderboard)

        val employeeList = listOf(
            LeaderboardAdapter.Employee(
                R.drawable.user,
                "John Doe",
                150,
                8,
                1
            ),
            LeaderboardAdapter.Employee(
                R.drawable.user,
                "Jane Smith",
                120,
                6,
                2
            ),
            LeaderboardAdapter.Employee(
                R.drawable.user,
                "Mike Johnson",
                100,
                5,
                3
            ),
            LeaderboardAdapter.Employee(
                R.drawable.user,
                "Emily Brown",
                90,
                4,
                4
            )
        )

        val layoutManager = LinearLayoutManager(this)
        recyclerViewLeaderboard.layoutManager = layoutManager

        val adapter = LeaderboardAdapter(employeeList)
        recyclerViewLeaderboard.adapter = adapter
    }
}

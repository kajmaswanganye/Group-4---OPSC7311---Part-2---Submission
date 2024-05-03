package com.example.chronolog_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaderboardAdapter(private val employeeList: List<Employee>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewEmployee: ImageView = itemView.findViewById(R.id.imageViewEmployee)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewHours: TextView = itemView.findViewById(R.id.textViewHours)
        val textViewProjects: TextView = itemView.findViewById(R.id.textViewProjects)
        val textViewRank: TextView = itemView.findViewById(R.id.textViewRank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.imageViewEmployee.setImageResource(employee.imageResId)
        holder.textViewName.text = employee.name
        holder.textViewHours.text = "Total Hours: ${employee.totalHoursWorked}"
        holder.textViewProjects.text = "Projects: ${employee.projectsWorked}"
        holder.textViewRank.text = "Rank: ${employee.rank}"
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    data class Employee(
        val imageResId: Int,
        val name: String,
        val totalHoursWorked: Int,
        val projectsWorked: Int,
        val rank: Int
    )
}

package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Find the ListView in the fragment layout
        val listViewCharts: ListView = view.findViewById(R.id.listView_charts)

        // Define the chart options
        val chartOptions = arrayOf("Pie Chart", "Bar Chart", "Line Chart")

        // Set up ArrayAdapter for the ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, chartOptions)
        listViewCharts.adapter = adapter

        // Set item click listener for the ListView
        listViewCharts.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    // Navigate to Pie Chart activity
                    startActivity(Intent(requireContext(), PieChartActivity::class.java))
                }
                1 -> {
                    // Navigate to Bar Chart activity
                    startActivity(Intent(requireContext(), BarChartActivity::class.java))
                }
                2 -> {
                    // Navigate to Line Chart activity
                    startActivity(Intent(requireContext(), LineChartActivity::class.java))
                }
            }
        }

        return view
    }
}

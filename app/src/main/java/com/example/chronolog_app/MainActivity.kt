package com.example.chronolog_app

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)!!
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.timesheet -> {
                    loadFragment(TimesheetFragment())
                    true
                }
                R.id.statistics -> {
                    loadFragment(StatisticsFragment())
                    true
                }
                R.id.dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }

                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true
                }

                else -> {
                    // Handle unhandled menu item selection here
                    Log.w(TAG, "Unhandled menu item selected: ${bottomNav}")
                    // Optionally, show a toast message
                    Toast.makeText(this@MainActivity, "Unhandled menu item selected", Toast.LENGTH_SHORT).show()
                    false // Return false to indicate that the item selection is not handled
                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}

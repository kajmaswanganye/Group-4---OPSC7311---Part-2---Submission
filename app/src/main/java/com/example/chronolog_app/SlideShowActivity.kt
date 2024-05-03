package com.example.chronolog_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class SlideshowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slideshow)

        // List of requirements
        val requirements = listOf(
            RequirementData(
                R.drawable.real_time_collaboration,
                "Real-Time Collaboration",
                "Implement real-time editing functionality to allow multiple users to edit the same spreadsheet simultaneously."
            ),
            RequirementData(
                R.drawable.natural_language,
                "Intuitive Natural Language Interface",
                "Develop a user-friendly natural language interface for interacting with the spreadsheet using conversational commands."
            ),
            RequirementData(
                R.drawable.advanced_visualisation,
                "Advanced Visualization Tools",
                "Integrate a variety of advanced visualization tools for creating charts, graphs, and dashboards."
            ),
            RequirementData(
                R.drawable.offline,
                "Offline Data Analysis",
                "Provide robust offline capabilities for working with data without an internet connection."
            ),
            RequirementData(
                R.drawable.security,
                "Enhanced Security Features",
                "Incorporate advanced security measures to protect sensitive data."
            ),
            RequirementData(
                R.drawable.cross_platform,
                "Cross-Platform Compatibility",
                "Ensure the application is accessible across multiple platforms, including web browsers, desktops, and mobile devices."
            ),
            RequirementData(
                R.drawable.easy,
                "Easy to Use",
                "Design the app to be beginner-friendly and easy to incorporate into daily workflows."
            ),
            RequirementData(
                R.drawable.game,
                "Gamification",
                "Introduce gamification elements to enhance user engagement and motivation."
            )
        )

        // Create fragments for each requirement
        val fragments = requirements.map { requirement ->
            SlideshowFragment.newInstance(
                requirement.imageResId,
                requirement.heading,
                requirement.details
            )
        }

        // Set up ViewPager with FragmentPagerAdapter
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = SlidePagerAdapter(supportFragmentManager, fragments)

        // Add ViewPager listener to detect when last slide is reached
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Not needed for this implementation
            }

            override fun onPageSelected(position: Int) {
                if (position == fragments.size - 1) {
                    // Last slide reached, navigate to SignupActivity
                    navigateToSignupActivity()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })
    }

    private fun navigateToSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent returning to the slideshow
    }

    data class RequirementData(val imageResId: Int, val heading: String, val details: String)

    class SlidePagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}
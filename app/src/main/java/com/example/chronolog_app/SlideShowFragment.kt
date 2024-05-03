package com.example.chronolog_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class SlideshowFragment : Fragment() {

    private var imageResId: Int = 0
    private var heading: String? = null
    private var details: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_slideshow, container, false)

        // Bind views
        val imageView = rootView.findViewById<ImageView>(R.id.imageViewSlideshow)
        val textViewHeading = rootView.findViewById<TextView>(R.id.textViewHeading)
        val textViewDetails = rootView.findViewById<TextView>(R.id.textViewDetails)

        // Set data to views
        imageView.setImageResource(imageResId)
        textViewHeading.text = heading
        textViewDetails.text = details

        return rootView
    }

    companion object {
        fun newInstance(imageResId: Int, heading: String, details: String): SlideshowFragment {
            val fragment = SlideshowFragment()
            fragment.imageResId = imageResId
            fragment.heading = heading
            fragment.details = details
            return fragment
        }
    }
}

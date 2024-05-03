package com.example.chronolog_app

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class TimesheetFragment : Fragment() {

    private lateinit var parentLayout: LinearLayout
    private var categoryCardViewCounter = 0
    private val categorySet = mutableSetOf<String>()
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var totalHoursTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timesheet, container, false)

        parentLayout = view.findViewById(R.id.Category_parent_layout)

        totalHoursTextView = TextView(requireContext())

        restoreCategories()

        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            showAddCategoryDialog()
        }

        return view
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter Category Name")
        val input = EditText(requireContext())
        input.hint = "Category Name"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val categoryName = input.text.toString()
            createButtonSetCategory(categoryName)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun restoreCategories() {
        val sharedPreferences =
            requireContext().getSharedPreferences("CategoryData", Context.MODE_PRIVATE)
        val count = sharedPreferences.getInt("categoryCount", 0)
        for (i in 1..count) {
            val categoryName = sharedPreferences.getString("categoryName_$i", "") ?: ""
            if (categoryName.isNotEmpty()) {
                categorySet.add(categoryName)
                createButtonSetCategory(categoryName)
            }
        }
    }

    private fun createButtonSetCategory(categoryName: String) {
        categoryCardViewCounter++

        val categoryCardView = CardView(requireContext())
        categoryCardView.id = categoryCardViewCounter

        val cardViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardViewLayoutParams.setMargins(0, 0, 0, 16) // Add bottom margin to CardView
        categoryCardView.layoutParams = cardViewLayoutParams
        categoryCardView.radius = 16f
        categoryCardView.setContentPadding(16, 16, 16, 16)

        val buttonSetLayout = LinearLayout(requireContext())
        buttonSetLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonSetLayout.orientation = LinearLayout.VERTICAL

        // Create RelativeLayout for category name and refresh button
        val relativeLayout = RelativeLayout(requireContext())
        val relativeLayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.layoutParams = relativeLayoutParams

        // Create TextView for category name
        val textView = TextView(requireContext())
        textView.id = View.generateViewId()
        textView.setTextSize(25F)
        textView.gravity = Gravity.CENTER
        textView.text = categoryName
        relativeLayout.addView(textView)

        // Create the refresh button
        val refreshButton = Button(requireContext())
        val refreshButtonParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        refreshButtonParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        refreshButton.layoutParams = refreshButtonParams
        refreshButton.text = "⟲"
        refreshButton.setOnClickListener {
            // Update text of totalHoursTextView
            val hours = requireActivity().intent.getIntExtra("hours", 0)
            updateTotalHoursText(hours)
        }
        relativeLayout.addView(refreshButton)

        // Add RelativeLayout to buttonSetLayout
        buttonSetLayout.addView(relativeLayout)

        // Create TextView for displaying total hours
        val totalHoursTextView = TextView(requireContext())
        totalHoursTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        totalHoursTextView.gravity = Gravity.CENTER
        val hours = requireActivity().intent.getIntExtra("hours", 0)
        Log.d("LogHours", "Received hours: $hours")
        totalHoursTextView.text = "Total Hours : $hours"
        buttonSetLayout.addView(totalHoursTextView)

        // Create ImageView for category image
        val imageView = ImageView(requireContext())
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imageView.tag = "image_view_$categoryCardViewCounter"
        buttonSetLayout.addView(imageView)

        // Button to add picture
        val addPictureButton = Button(requireContext())
        addPictureButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        addPictureButton.text = "Add Picture"
        addPictureButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }
        buttonSetLayout.addView(addPictureButton)

        // Button to remove category
        val removeCategoryButton = Button(requireContext())
        removeCategoryButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        removeCategoryButton.text = "Remove Category"
        removeCategoryButton.setOnClickListener {
            removeCategory(categoryName, categoryCardView.id)
        }
        buttonSetLayout.addView(removeCategoryButton)

        // View Tasks Button
        val buttonsLayout = LinearLayout(requireContext())
        buttonsLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonsLayout.orientation = LinearLayout.HORIZONTAL

        val viewTasksButton = createButton("View Tasks", categoryCardViewCounter, categoryName)
        buttonsLayout.addView(viewTasksButton)

        // Activity Log Button
        val activityLogButton = Button(requireContext())
        activityLogButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        activityLogButton.text = "Activity Log"
        activityLogButton.setOnClickListener {
            // Show a dialog with the specified message
            showDialog("Features coming in next update :)")
        }
        buttonSetLayout.addView(activityLogButton)

        buttonSetLayout.addView(buttonsLayout)
        categoryCardView.addView(buttonSetLayout)
        parentLayout.addView(categoryCardView)
    }

    private fun updateTotalHoursText(hours: Int) {
        totalHoursTextView.text = "Total Hours: $hours"
    }

    private fun createButton(
        text: String,
        categoryCardViewCounter: Int,
        categoryName: String
    ): Button {
        val button = Button(requireContext())
        val buttonLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        button.layoutParams = buttonLayoutParams
        button.text = text
        // Button click events
        button.setOnClickListener {
            if (text == "View Tasks") {
                val intent = Intent(requireContext(), TaskActivity::class.java)
                intent.putExtra("categoryName", categoryName)
                startActivity(intent)
            }
        }
        return button
    }

    private fun removeCategory(categoryName: String, categoryCardViewId: Int) {
        val sharedPreferences =
            requireContext().getSharedPreferences("CategoryData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val count = sharedPreferences.getInt("categoryCount", 0)
        for (i in 1..count) {
            val savedCategoryName = sharedPreferences.getString("categoryName_$i", "") ?: ""
            if (savedCategoryName == categoryName) {
                editor.remove("categoryName_$i")
                editor.remove("categoryImage_$categoryCardViewId")
                editor.apply()
                categorySet.remove(categoryName)
                parentLayout.removeView(requireActivity().findViewById(categoryCardViewId))
                break
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            // Set the selected image to the ImageView
            val imageView =
                parentLayout.findViewWithTag("image_view_$categoryCardViewCounter") as ImageView
            imageView.setImageURI(selectedImageUri)
            // Save the image URI to SharedPreferences
            saveImageUri(selectedImageUri.toString(), categoryCardViewCounter.toString())
        }
    }

    private fun saveImageUri(imageUri: String, categoryCardViewCounter: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("CategoryData", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("categoryImage_$categoryCardViewCounter", imageUri)
            .apply()
    }

    private fun showDialog(message: String) {
        // Create an AlertDialog
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK button is clicked
                dialog.dismiss()
            }
            .show()
    }
}
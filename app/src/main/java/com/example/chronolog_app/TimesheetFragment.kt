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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TimesheetFragment : Fragment() {

    private lateinit var parentLayout: LinearLayout
    private var categoryCardViewCounter = 0
    private val categorySet = mutableSetOf<String>()
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var totalHoursTextView: TextView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timesheet, container, false)

        parentLayout = view.findViewById(R.id.Category_parent_layout)
        totalHoursTextView = TextView(requireContext())

        database = FirebaseDatabase.getInstance().reference

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
        database.child("categories").get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { categorySnapshot ->
                val categoryName = categorySnapshot.key ?: return@forEach
                categorySet.add(categoryName)
                createButtonSetCategory(categoryName)
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Error getting categories", it)
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

        // Save category to Firebase
        database.child("categories").child(categoryName).setValue(true)
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
        database.child("categories").child(categoryName).removeValue().addOnSuccessListener {
            categorySet.remove(categoryName)
            parentLayout.removeView(requireActivity().findViewById(categoryCardViewId))
        }.addOnFailureListener {
            Log.e("Firebase", "Error removing category", it)
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
            // Save the image URI to Firebase
            saveImageUri(selectedImageUri.toString(), categoryCardViewCounter.toString())
        }
    }

    private fun saveImageUri(imageUri: String, categoryCardViewCounter: String) {
        database.child("images").child(categoryCardViewCounter).setValue(imageUri)
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

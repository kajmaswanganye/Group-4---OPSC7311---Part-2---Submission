package com.example.chronolog_app

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*

class SetDailyGoalsActivity : AppCompatActivity() {

    private lateinit var min: EditText
    private lateinit var max: EditText
    private lateinit var saveBtn: Button
    private lateinit var viewBtn: Button
    private lateinit var lineChart: LineChart
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_daily_goals)

        min = findViewById(R.id.editTextMinGoal)
        max = findViewById(R.id.editTextMaxGoal)
        saveBtn = findViewById(R.id.buttonSave)
        viewBtn = findViewById(R.id.buttonView)
        lineChart = findViewById(R.id.lineChart)

        database = FirebaseDatabase.getInstance().reference

        saveBtn.setOnClickListener {
            saveGoal()
        }

        viewBtn.setOnClickListener {
            viewGoal()
        }
    }

    private fun saveGoal() {
        val minGoal = min.text.toString().toDoubleOrNull() ?: 0.0
        val maxGoal = max.text.toString().toDoubleOrNull() ?: 0.0

        val goalData = mapOf(
            "minGoal" to minGoal,
            "maxGoal" to maxGoal
        )

        database.child("dailyGoals").setValue(goalData)
            .addOnSuccessListener {
                Toast.makeText(this, "Goals saved successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save goals: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun viewGoal() {
        database.child("dailyGoals").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val minGoal = snapshot.child("minGoal").getValue(Double::class.java) ?: 0.0
                val maxGoal = snapshot.child("maxGoal").getValue(Double::class.java) ?: 0.0

                renderData(minGoal.toFloat(), maxGoal.toFloat())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SetDailyGoalsActivity, "Failed to load goals.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun renderData(minGoal: Float, maxGoal: Float) {
        val llXAxis = LimitLine(10f, "Index 10")
        llXAxis.lineWidth = 4f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f

        val xAxis = lineChart.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.axisMaximum = 10f
        xAxis.axisMinimum = 0f
        xAxis.setDrawLimitLinesBehindData(true)

        val ll1 = LimitLine(maxGoal, "Maximum Limit")
        ll1.lineWidth = 4f
        ll1.enableDashedLine(10f, 10f, 0f)
        ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        ll1.textSize = 10f

        val ll2 = LimitLine(minGoal, "Minimum Limit")
        ll2.lineWidth = 4f
        ll2.enableDashedLine(10f, 10f, 0f)
        ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        ll2.textSize = 10f

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()
        leftAxis.addLimitLine(ll1)
        leftAxis.addLimitLine(ll2)
        leftAxis.axisMaximum = maxGoal + 50f
        leftAxis.axisMinimum = 0f
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(false)
        leftAxis.setDrawLimitLinesBehindData(false)

        lineChart.axisRight.isEnabled = false
        setData(minGoal, maxGoal)
    }

    private fun setData(minGoal: Float, maxGoal: Float) {
        val values = ArrayList<Entry>()
        // Replace this with actual data points as needed
        values.add(Entry(1f, minGoal))
        values.add(Entry(2f, maxGoal))
        values.add(Entry(3f, (minGoal + maxGoal) / 2))
        values.add(Entry(4f, maxGoal - 10))
        values.add(Entry(5f, minGoal + 10))

        val set1: LineDataSet
        if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
            set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            lineChart.data.notifyDataChanged()
            lineChart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "Daily Goals")
            set1.setDrawIcons(false)
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.DKGRAY
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            val drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue)
            set1.fillDrawable = drawable

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)

            val data = LineData(dataSets)
            lineChart.data = data
        }
    }
}

package com.kanaeva.mobilecursach

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kanaeva.mobilecursach.rest.response.ResultTestDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_test_result.*

class TestResult : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    var previousPage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        val resultTest : ResultTestDto = intent.getSerializableExtra("result") as ResultTestDto
        previousPage = intent.getStringExtra("previousPage")!!

        //клик по кнопке "профиль"
        profile.setOnClickListener {
            // проверка авторизации
            sessionManager = SessionManager(this)
            var token :String? = sessionManager.getAuthToken()
            if (token != null) {
                val intent = Intent(this, Personal::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        }

        //клик по кнопке "тест"
        Test.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setBarChart(resultTest)
    }
 // привет
    private fun setBarChart(result: ResultTestDto) {

        var xAxis = testResultChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = PsychotypeFormatter()

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, result.extraversionType.toFloat()))
        entries.add(BarEntry(2f, result.spontaneityType.toFloat()))
        entries.add(BarEntry(3f, result.aggressivenessType.toFloat()))
        entries.add(BarEntry(4f, result.rigidityType.toFloat()))
        entries.add(BarEntry(5f, result.introversionType.toFloat()))
        entries.add(BarEntry(6f, result.sensitivityType.toFloat()))
        entries.add(BarEntry(7f, result.anxietyType.toFloat()))
        entries.add(BarEntry(8f, result.labilityType.toFloat()))
        entries.add(BarEntry(9f, result.lieType.toFloat()))
        entries.add(BarEntry(10f, result.aggravationType.toFloat()))


        val dataSet = BarDataSet(entries, "Результат")

        val data = BarData(dataSet)
        testResultChart.data = data
        testResultChart.setFitBars(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = 10
        testResultChart.invalidate()
        testResultChart.animate()
    }
    class PsychotypeFormatter : ValueFormatter() {

        final val xLabel  = listOf("Экстраверсия",
            "Спонтанность",
            "Агрессивность",
            "Ригидность",
            "Интроверсия",
            "Сензитивность",
            "Тревожность",
            "Лабильность",
            "Ложь",
            "Аггравация"
        )

        override fun getFormattedValue(value: Float): String {
            return xLabel [value.toInt()-1]
        }
    }
}


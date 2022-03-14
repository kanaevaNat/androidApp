package com.kanaeva.mobilecursach.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import com.kanaeva.mobilecursach.R

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
package com.kanaeva.mobilecursach.rest.model

data class Question(
    val id: Long,
    val question: String,
    val isAdd: Boolean,
    val questionTypeId: Long
)

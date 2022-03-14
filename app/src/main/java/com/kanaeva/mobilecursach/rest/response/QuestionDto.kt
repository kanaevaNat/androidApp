package com.kanaeva.mobilecursach.rest.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

public class QuestionDto: Serializable {

    @SerializedName("id")
    val id: Long = 0

    @SerializedName("question")
    val question: String = ""

    @SerializedName("isAdd")
    private val isAdd: Boolean = false

    @SerializedName("questionTypeId")
    private val questionTypeId: Long = 0
}
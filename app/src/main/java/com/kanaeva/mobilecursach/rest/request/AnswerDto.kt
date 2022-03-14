package com.kanaeva.mobilecursach.rest.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AnswerDto: Serializable {

    @SerializedName("answer")
    var answer: Boolean = false

    @SerializedName("questionId")
    var questionId: Long = 0

    constructor(answer: Boolean, questionId: Long) {
        this.answer = answer
        this.questionId = questionId
    }
}
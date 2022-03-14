package com.kanaeva.mobilecursach

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.request.AnswerDto
import com.kanaeva.mobilecursach.rest.response.QuestionDto
import com.kanaeva.mobilecursach.rest.response.ResultTestDto
import kotlinx.android.synthetic.main.activity_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Question : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var timer : CountDownTimer
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        this.context = this

        apiClient = ApiClient()

        var questions : ArrayList<QuestionDto> = intent.getSerializableExtra("questions") as ArrayList<QuestionDto>
        var currentQuestionNumber = 1;
        var currentQuestion: QuestionDto = questions[currentQuestionNumber - 1];
        question.text = currentQuestion.question;
        val questio = getString(R.string.questio);
        questionNumber.text = questio + " " + currentQuestionNumber + "/" + questions.size
        var selectedAnswer: Boolean = false;
        goNext.isEnabled = false
        var answers: ArrayList<AnswerDto> = ArrayList()


        this.timer = object: CountDownTimer(420000L, 1000) {
            override fun onFinish() {
                val time = getString(R.string.time)
                Toast.makeText( context, time, Toast.LENGTH_SHORT).show()
                val stopTestDialog = AlertDialog.Builder(context)
                val time_test_new = getString(R.string.time_test_new)
                stopTestDialog.setTitle(time_test_new)
                val ok = getString(R.string.ok)
                stopTestDialog.setPositiveButton(ok, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(context, TestDescriptionActivity::class.java)
                        startActivity(intent)
                    }
                })
                stopTestDialog.setCancelable(false)
                stopTestDialog.show()


            }

            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished - minutes*60000) / 1000
                stopwatch.text = minutes.toString() + ":" + seconds.toString()
            }

        }
        timer.start()
        // выбор ответа "да"
        selectedYes.setOnClickListener {
            selectedYes.background =  getResources().getDrawable(R.drawable.yellow)
            selectedNo.background =  getResources().getDrawable(R.color.yellow)
            selectedAnswer = true;
            goNext.isEnabled = true
            goNext.visibility = View.VISIBLE
        }


        // выбор ответа "нет"
        selectedNo.setOnClickListener {
            selectedNo.background =  getResources().getDrawable(R.drawable.yellow)
            selectedYes.background =  getResources().getDrawable(R.color.yellow)
            selectedAnswer = false
            goNext.isEnabled = true
            goNext.visibility = View.VISIBLE

        }

        // клик по кнопке "вперед"
        goNext.setOnClickListener {
            goNext.isEnabled = false
            if (currentQuestionNumber < questions.size) {
                currentQuestionNumber++;
                if (currentQuestionNumber === questions.size) {
                    val in_result = getString(R.string.in_result)
                    goNext.text = in_result

                }

                currentQuestion = questions[currentQuestionNumber -1]
                question.text = currentQuestion.question

                questionNumber.text = questio + " "+ currentQuestionNumber + "/" + questions.size

                goNext.visibility = View.INVISIBLE
                selectedNo.background =  getResources().getDrawable(R.color.yellow)
                selectedYes.background =  getResources().getDrawable(R.color.yellow)
                val answer = AnswerDto(selectedAnswer, currentQuestion.id)
                answers.add(answer)
            } else {
                val answer = AnswerDto(selectedAnswer, currentQuestion.id)
                answers.add(answer)
                apiClient.getApiService(this).solveTest(answers)
                    .enqueue( object  : Callback<ResultTestDto> {
                        override fun onFailure(call: Call<ResultTestDto>, t: Throwable) {
                            println(t.stackTrace)
                            println("****************************************")
                            println(t.message)
                            println("****************************************")
                            println(t.localizedMessage)
                            println("****************************************")
                            Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<ResultTestDto>,
                            response: Response<ResultTestDto>
                        ) {
                            val result = response.body();
                            val intent = Intent(context, TestResult::class.java)
                            intent.putExtra("result", result)
                            intent.putExtra("previousPage", "test_desc")
                            startActivity(intent)
                            println(result)
                            val answer_saved = getString(R.string.answer_saved)
                            Toast.makeText( context, answer_saved, Toast.LENGTH_SHORT).show()
                        }
                    })
            }

        }
    }

    override fun onBackPressed() {
        val stopTestDialog = AlertDialog.Builder(this)
        val quit_test = getString(R.string.quit_test)
        val yes = getString(R.string.yes)
        val no = getString(R.string.no)
        stopTestDialog.setTitle(quit_test)
        stopTestDialog.setPositiveButton(yes, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        });
        stopTestDialog.setNegativeButton(no, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, whitch: Int) {
                dialog?.cancel()
            }
        })
        stopTestDialog.show()
    }
}
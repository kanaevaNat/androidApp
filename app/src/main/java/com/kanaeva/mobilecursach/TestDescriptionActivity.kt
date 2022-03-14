package com.kanaeva.mobilecursach

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.response.QuestionDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_test_description.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestDescriptionActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_description)

        val context = this

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        // клик по кнопке "Начать тест" - переход на страницу первого вопроса
        goTest.setOnClickListener {
            apiClient.getApiService(this).getQuestions()
                .enqueue(object : Callback<ArrayList<QuestionDto>> {
                    override fun onFailure(call: Call<ArrayList<QuestionDto>>, t: Throwable) {
                        Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ArrayList<QuestionDto>>, response: Response<ArrayList<QuestionDto>>) {
                        val questions = response.body();

                        if (questions != null && questions.isNotEmpty()) {
                            val intent = Intent(context, Question::class.java)
                            intent.putExtra("questions", questions)
                            startActivity(intent)
                        } else {
                            Toast.makeText( context,"Вопросы не найдены", Toast.LENGTH_LONG).show()
                        }
                    }
                })

        }

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
    }
}
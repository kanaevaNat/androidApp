package com.kanaeva.mobilecursach

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.response.ResultTestDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_personal.Test
import kotlinx.android.synthetic.main.activity_personal.profile
import kotlinx.android.synthetic.main.activity_stat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class StatActivity : AppCompatActivity() {

    var size = 10
    var page = 0
    val context = this
    val apiClient = ApiClient()
    var pagesCount = 0
    var totalPages = 0
    private var userId = ""
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        this.userId = getIntent().getExtras()?.getString("userId")!!
        var isAdmin = getIntent().getExtras()?.getBoolean("isAdmin")!!
        if (isAdmin) userId = ""
        // получение данных
        if (userId != null) {
            this.loadStatistic()
        }

        previousPage.setOnClickListener {
            this.page--
            this.loadStatistic()
        }

        nextPage.setOnClickListener {
            this.page++
            this.loadStatistic()
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

    fun loadStatistic() {
        apiClient.getApiService(this).getResults(this.userId, this.page, this.size)
            .enqueue(object : Callback<ArrayList<ResultTestDto>> {
                override fun onFailure(call: Call<ArrayList<ResultTestDto>>, t: Throwable) {
                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                @SuppressLint("ResourceAsColor")
                override fun onResponse(call: Call<ArrayList<ResultTestDto>>, response: Response<ArrayList<ResultTestDto>>) {
                    var totalCount = response.headers() ["x-total-count"]!!.toInt()
                    pagesCount = ceil((totalCount.toFloat()) / size).toInt()
                    totalPages = pagesCount
                    pages.text = (page +1).toString() + "/" + pagesCount.toString()
                    if (pagesCount-1 === page) {
                        nextPage.isEnabled = false
                        nextPage.visibility = View.INVISIBLE
                    } else {
                        nextPage.isEnabled = true
                        nextPage.visibility = View.VISIBLE
                    }

                    if (page === 0) {
                        previousPage.isEnabled = false
                        previousPage.visibility = View.INVISIBLE
                    } else {
                        previousPage.isEnabled = true
                        previousPage.visibility = View.VISIBLE
                    }

                    btns.removeAllViews()
                    var resultInfo = response.body()
                    var i = 1
                    resultInfo?.forEach{
                        var btn = Button(context)
                        btn.id = it.id.toInt()
                        if (i % 2 == 0) {
                            btn.setBackgroundColor(resources.getColor(R.color.mainOrangeBar))
                        } else {
                            btn.setBackgroundColor(resources.getColor(R.color.mainOrangeBar))
                        }

                        val instant = Instant.parse(it.finishedAt)
                        val formatter: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm").withZone(ZoneId.systemDefault())
                        btn.setText("Результат за " + formatter.format(instant))

                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        val scrollView = findViewById<ScrollView>(R.id.results)
                        params.setMargins(50, 0, 50, 30)
                        params.gravity = Gravity.CENTER
                        btn.layoutParams = params

                        btn.gravity = Gravity.CENTER_HORIZONTAL

                        // получение конкретного результата
                        btn.setOnClickListener({ v ->
                            val intent = Intent(context, TestResult::class.java)
                            intent.putExtra("result", it)
                            intent.putExtra("previousPage", "stat")
                            startActivity(intent)
                        })

                        //mainLayout.addView(btn)
                        btns.addView(btn)
                        i++
                    }
                }
            })
    }
}
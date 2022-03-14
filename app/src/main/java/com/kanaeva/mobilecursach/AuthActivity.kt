package com.kanaeva.mobilecursach

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.request.LoginDto
import com.kanaeva.mobilecursach.rest.response.TokenDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // авторизация
        login.setOnClickListener {
            val context = this
            // инициализация апи сервиса
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var email : String = userEmail.getText().toString()
            var pass : String = userPassword.getText().toString()
            if (email.length == 0 || pass.length == 0) {
                val enter_email_password = getString(R.string.enter_email_password)
                Toast.makeText( context, enter_email_password, Toast.LENGTH_SHORT).show()
            } else {
                apiClient.getApiService(this).authenticate(LoginDto(username = email, password = pass))
                    .enqueue(object : Callback<TokenDto> {
                        override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                            Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                            val loginResponse = response.body()

                            if (loginResponse?.token != null) {
                                sessionManager.saveAuthToken(loginResponse.token)
                                val successfully = getString(R.string.successfully)
                                Toast.makeText( context, successfully, Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, Personal::class.java)
                                startActivity(intent)
                            } else {
                                val incorrect = getString(R.string.incorrect)
                                Toast.makeText( context, incorrect, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }
        }

        // регистрация
        registration.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        // забыли пароль
        resetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
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
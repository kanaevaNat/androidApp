package com.kanaeva.mobilecursach

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.request.ResetPassDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_auth.Test
import kotlinx.android.synthetic.main.activity_auth.profile
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // отправить
        sendEmail.setOnClickListener {

            apiClient = ApiClient()
            val context = this
            var email : String = emailInput.getText().toString()

            if (email?.length == 0) {
                val enter_email = getString(R.string.enter_email)
                Toast.makeText( context, enter_email, Toast.LENGTH_SHORT).show()
            } else {
                apiClient.getApiService(this).resetPassword(ResetPassDto(mail = emailInput?.getText().toString()))
                    .enqueue(object : Callback<Void> {
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.code() == 200) {
                                val new_password_email = getString(R.string.new_password_email)
                                Toast.makeText( context, new_password_email, Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            } else {
                                val user_not_found = getString(R.string.user_not_found)
                                Toast.makeText( context, user_not_found, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
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
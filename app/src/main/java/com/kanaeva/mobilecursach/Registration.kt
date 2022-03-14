package com.kanaeva.mobilecursach

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.request.LoginDto
import com.kanaeva.mobilecursach.rest.response.AccountDto
import com.kanaeva.mobilecursach.rest.response.TokenDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.util.*


class Registration: AppCompatActivity() {

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val calendar = Calendar.getInstance()

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

        // выбор даты рождения через календарь
        birthdayInput.setOnClickListener {
            val dataPicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                birthdayInput.setText("" + dayOfMonth + "." + (monthOfYear+1) + "." + year)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dataPicker.show()
        }

        // регистрация
        register.setOnClickListener {
            val context = this

            val email = emailInput.text.toString()
            val passwordFirst = passwordInput.text.toString()
            val passwordSecond = passwordRepeatInput.text.toString()
            val gender = resources.getResourceEntryName(genderSelect.checkedRadioButtonId)
            val firstName = nameInput.text.toString()
            val createdDate = Instant.now().toString()
            val birthday = birthdayInput.text.toString()

            if (
                email.length == 0 ||
                passwordFirst.length == 0 ||
                passwordSecond.length == 0 ||
                birthday.length == 0 ||
                gender.length == 0 ||
                firstName.length == 0
            ) {
                val fields_required = getString(R.string.fields_required)
                Toast.makeText( context, fields_required, Toast.LENGTH_SHORT).show()
            } else {
                if (passwordFirst.length < 8 || passwordSecond.length < 8) {
                    val length_password = getString(R.string.length_password)
                    Toast.makeText( context, length_password, Toast.LENGTH_SHORT).show()
                } else {
                    if (passwordFirst != passwordSecond) {
                        val match_password = getString(R.string.match_password)
                        Toast.makeText( context, match_password, Toast.LENGTH_SHORT).show()
                    } else {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            val incorrect_email = getString(R.string.incorrect_email)
                            Toast.makeText( context, incorrect_email, Toast.LENGTH_SHORT).show()
                        } else {
                            apiClient = ApiClient()
                            sessionManager = SessionManager(this)

                            var body = AccountDto(
                                login = email,
                                email = email,
                                password = passwordFirst,
                                activated = true,
                                firstName = firstName,
                                gender = gender,
                                birthDate = birthday,
                                createdDate = createdDate,
                                authorities = ArrayList<String>()
                            )

                            apiClient.getApiService(this).registration(body)
                                .enqueue(object : Callback<Void> {
                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                                        Log.e("1", "1")
                                    }

                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.code() == 201) {
                                            val successfully_registered = getString(R.string.successfully_registered)
                                            Toast.makeText( context, successfully_registered, Toast.LENGTH_SHORT).show()
                                            apiClient.getApiService(context).authenticate(LoginDto(username = email, password = passwordFirst))
                                                .enqueue(object : Callback<TokenDto> {
                                                    override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                                                        Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                                                    }
                                                    override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                                                        val loginResponse = response.body()

                                                        if (loginResponse?.token != null) {
                                                            sessionManager.saveAuthToken(loginResponse.token)
                                                            val intent = Intent(context, Personal::class.java)
                                                            startActivity(intent)
                                                        } else {
                                                            val fail_auth = getString(R.string.fail_auth)
                                                            Toast.makeText( context,fail_auth, Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                                })
                                        }
                                        if (response.code() == 400) {
                                            Toast.makeText( context,response.message().toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                })
                        }
                    }
                }
            }
        }
    }
}
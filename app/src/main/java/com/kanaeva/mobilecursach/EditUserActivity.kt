package com.kanaeva.mobilecursach

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.request.PasswordDto
import com.kanaeva.mobilecursach.rest.response.AccountDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_edit_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditUserActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var apiClient: ApiClient
        sessionManager = SessionManager(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        //исходные данные
        apiClient = ApiClient()
        val context = this
        apiClient.getApiService(this).getAccount()
            .enqueue(object : Callback<AccountDto> {
                override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                    val userInfo = response.body()
                    emailInput.setText(userInfo?.email)
                    nameInput.setText(userInfo?.firstName)
                    birthdayInput.setText(userInfo?.birthDate)
                    if (userInfo?.gender == "MALE") {
                        genderSelect.check(MALE.id)
                    } else {
                        genderSelect.check(FEMALE.id)
                    }
                    userId = userInfo?.id!!
                }
            })

        // выбор даты рождения через календарь
        val calendar = Calendar.getInstance()
        birthdayInput.setOnClickListener {
            val dataPicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                view, year, monthOfYear, dayOfMonth ->
                birthdayInput.setText("" + dayOfMonth + "." + (monthOfYear+1) + "." + year)
            }, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dataPicker.show()
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

        // клик по кнопке "сохранить"
        save.setOnClickListener {
            apiClient = ApiClient()
            val context = this

            val email = emailInput.text.toString()
            val passwordOld = oldPassword.text.toString()
            val passwordFirst = passwordInput.text.toString()
            val passwordSecond = passwordRepeatInput.text.toString()
            val gender = resources.getResourceEntryName(genderSelect.checkedRadioButtonId)
            val name = nameInput.text.toString()
            val birthday = birthdayInput.text.toString()

            // смена пароля если введены новые
            if (passwordFirst.length != 0 || passwordSecond.length != 0 || passwordOld.length != 0) {
                if (passwordFirst.length == 0 || passwordSecond.length == 0 || passwordOld.length == 0) {
                    val opassword_and_npassword = getString(R.string.opassword_and_npassword)
                    Toast.makeText( context, opassword_and_npassword, Toast.LENGTH_SHORT).show()
                } else {
                    if (passwordFirst.length != passwordSecond.length) {
                        val match_password = getString(R.string.match_password)
                        Toast.makeText( context, match_password, Toast.LENGTH_SHORT).show()
                    } else {
                        if (passwordFirst.length < 8) {
                            val length_password = getString(R.string.length_password)
                            Toast.makeText( context, length_password, Toast.LENGTH_SHORT).show()
                        } else {
                            var pass = PasswordDto(
                                currentPassword = passwordOld,
                                newPassword = passwordFirst
                            )
                            apiClient.getApiService(this).changePassword(pass)
                                .enqueue(object : Callback<Void> {
                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.code() == 200) {
                                            val password_successfully = getString(R.string.password_successfully)
                                            Toast.makeText( context, password_successfully, Toast.LENGTH_SHORT).show()
                                        } else {
                                            val opassword_incorrect = getString(R.string.opassword_incorrect)
                                            Toast.makeText( context, opassword_incorrect, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })
                        }
                    }
                }

            }

            // изменение данных пользователя
            if (
                email.length == 0 ||
                birthday.length == 0 ||
                gender.length == 0 ||
                name.length == 0
            ) {
                Toast.makeText( context, "Все поля обязательны для заполнения", Toast.LENGTH_SHORT).show()
            } else {
                var body = AccountDto(
                    login = email,
                    email = email,
                    firstName = name,
                    gender = gender,
                    birthDate = birthday,
                    id = userId,
                    authorities = ArrayList()
                )
                Log.e("body", gender)
                Log.e("body", birthday)
                apiClient.getApiService(this).updateUser(body)
                    .enqueue(object : Callback<Void> {
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.code() == 200) {
                                Toast.makeText( context,"Данные профиля успешно заменены", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText( context,"Произошла ошибка при изменении данных", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }


        }
    }
}
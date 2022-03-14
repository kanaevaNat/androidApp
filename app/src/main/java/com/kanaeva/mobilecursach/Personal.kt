package com.kanaeva.mobilecursach

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.rest.response.AccountDto
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_personal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Personal : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private var userId = 0
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var apiClient: ApiClient
        sessionManager = SessionManager(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)

        apiClient = ApiClient()
        val context = this
        apiClient.getApiService(this).getAccount()
            .enqueue(object : Callback<AccountDto> {
                override fun onFailure(call: Call<AccountDto>, t: Throwable) {
                    Toast.makeText( context,"FAIL", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<AccountDto>, response: Response<AccountDto>) {
                    val userInfo = response.body()
                    userEmail.setText(userInfo?.email)
                    userName.setText(userInfo?.firstName)
                    userBirtday.setText(userInfo?.birthDate)
                    userGender.setText(userInfo?.gender)
                    if (userInfo?.gender == "MALE") {
                        val male = getString(R.string.male)
                        userGender.setText(male)
                    } else {
                        val female = getString(R.string.female)
                        userGender.setText(female)
                    }

                    sessionManager.saveUserRoles(userInfo?.authorities)
                    if (userInfo?.authorities?.indexOf("ROLE_ADMIN") !== -1) {
                        isAdmin = true
                    }
                    println(sessionManager.getRoles())
                    userId = userInfo?.id!!
                }
            })

        // выход из личного кабинета
        logout.setOnClickListener {
            val context = this
            val quitDialog = AlertDialog.Builder(this)
            val quit_profile = getString(R.string.quit_profile)
            val yes = getString(R.string.yes)
            val no = getString(R.string.no)
            quitDialog.setTitle(quit_profile)
            quitDialog.setPositiveButton(yes, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    sessionManager.cleanAuthToken()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            })
            quitDialog.setNegativeButton(no, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.cancel()  //закрыть диалоговое окно
                }
            })
            quitDialog.show()
        }

        //клик по кнопке "тест"
        Test.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // клик по кнопке "статистика"
        getStat.setOnClickListener {
            val intent = Intent(this, StatActivity::class.java)
            intent.putExtra("userId", userId.toString())
            intent.putExtra("isAdmin", isAdmin)
            startActivity(intent)
        }

        // клик по кнопке "редактировать"
        editUserData.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
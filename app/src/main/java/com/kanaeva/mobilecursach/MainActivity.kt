package com.kanaeva.mobilecursach

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kanaeva.mobilecursach.rest.ApiClient
import com.kanaeva.mobilecursach.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*





class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // клик по кнопке "Начать тест" - переход на страницу описания теста
        goTest.setOnClickListener {
            val intent = Intent(this, TestDescriptionActivity::class.java)
            startActivity(intent)
        }

        // клик по кнопке "Личный кабинет"
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
    }

    // обработка нажатия кнопки "назад"
    override fun onBackPressed() {
        val quitDialog = AlertDialog.Builder(this)
        val quit_app = getString(R.string.quit_app)
        val yes = getString(R.string.yes)
        val no = getString(R.string.no)
        quitDialog.setTitle(quit_app)
        quitDialog.setPositiveButton(yes, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                finishAffinity() // закрыть приложение
            }
        })
        quitDialog.setNegativeButton(no, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.cancel()  //закрыть диалоговое окно
            }
        })
        quitDialog.show()
    }

}
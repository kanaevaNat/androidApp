package com.kanaeva.mobilecursach.rest

import com.kanaeva.mobilecursach.rest.request.AnswerDto
import com.kanaeva.mobilecursach.rest.request.LoginDto
import com.kanaeva.mobilecursach.rest.request.PasswordDto
import com.kanaeva.mobilecursach.rest.request.ResetPassDto
import com.kanaeva.mobilecursach.rest.response.AccountDto
import com.kanaeva.mobilecursach.rest.response.QuestionDto
import com.kanaeva.mobilecursach.rest.response.ResultTestDto
import com.kanaeva.mobilecursach.rest.response.TokenDto
import com.kanaeva.mobilecursach.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST(Constants.AUTH_URL)
    fun authenticate(@Body request: LoginDto): Call<TokenDto>

    @GET(Constants.ACCOUNT_URL)
    fun getAccount (): Call<AccountDto>

    @POST(Constants.REGISTER_URL)
    fun registration(@Body result: AccountDto): Call<Void>

    @POST(Constants.SOLVE_TEST_URL)
    fun solveTest(@Body answers: ArrayList<AnswerDto>): Call<ResultTestDto>

    @GET(Constants.QUESTIONS_URL)
    fun getQuestions(): Call<ArrayList<QuestionDto>>

    @GET(Constants.RESULTS_URL)
    fun getResults(@Query("userId.equals") id: String?, @Query("page") page: Int?, @Query("size") size: Int?): Call<ArrayList<ResultTestDto>>

    @POST(Constants.USER_UPDATE_URL)
    fun updateUser(@Body request: AccountDto): Call<Void>

    @POST(Constants.CHANGE_PASSWORD__URL)
    fun changePassword(@Body request: PasswordDto): Call<Void>

    @POST(Constants.RESET_PASSWORD_URL)
    fun resetPassword(@Body request: ResetPassDto): Call<Void>

}
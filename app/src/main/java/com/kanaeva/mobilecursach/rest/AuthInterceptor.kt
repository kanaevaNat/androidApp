package com.kanaeva.mobilecursach.rest

import android.content.Context
import com.kanaeva.mobilecursach.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (context: Context): Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.getAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
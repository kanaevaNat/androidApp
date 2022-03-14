package com.kanaeva.mobilecursach.utils

import android.content.Context
import android.content.SharedPreferences
import com.kanaeva.mobilecursach.R

class SessionManager (context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val  AUTH_TOKEN = "auth_token"
        const val ROLES = "roles"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    fun saveUserRoles(roles: ArrayList<String>?) {
        val editor = prefs.edit()
        editor.putString(roles.toString(), ROLES)
        editor.apply()
    }

    fun getRoles(): String? {
        return prefs.getString(ROLES, null)
    }

    fun getAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun cleanAuthToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
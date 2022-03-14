package com.kanaeva.mobilecursach.rest.response

import com.google.gson.annotations.SerializedName

data class AccountDto(
    @SerializedName("email")
    var email: String,

    @SerializedName("firstName")
    var firstName: String,

    @SerializedName("login")
    var login: String,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("gender")
    var gender: String,

    @SerializedName("birthDate")
    var birthDate: String,

    @SerializedName("createdDate")
    var createdDate: String? = null,

    @SerializedName("activated")
    var activated: Boolean? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("authorities")
    val authorities: ArrayList<String>
)

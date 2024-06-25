package com.dicoding.githubuser.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    val id : Int,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("login")
    val login: String
)
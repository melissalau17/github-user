package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.GithubResponse
import com.dicoding.ItemsItem
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowersData(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingData(@Path("username") username: String): Call<List<ItemsItem>>
}
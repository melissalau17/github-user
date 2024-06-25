package com.dicoding.githubuser.data

import com.dicoding.ItemsItem

interface UserRepository {
    suspend fun getFollowers(username: String, onSuccess: (List<ItemsItem>) -> Unit, onFailure: (String) -> Unit)
    suspend fun getFollowing(username: String, onSuccess: (List<ItemsItem>) -> Unit, onFailure: (String) -> Unit)
}

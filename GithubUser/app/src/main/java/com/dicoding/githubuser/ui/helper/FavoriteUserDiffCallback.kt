package com.dicoding.githubuser.ui.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuser.database.FavoriteUser

class FavoriteUserDiffCallback (private val oldFavoriteUserList: List<FavoriteUser>, private val newFavoriteUserList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteUserList.size
    override fun getNewListSize(): Int = newFavoriteUserList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUserList[oldItemPosition].username == newFavoriteUserList[newItemPosition].username
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavoriteUserList[oldItemPosition]
        val newNote = newFavoriteUserList[newItemPosition]
        return oldNote.username == newNote.username && oldNote.avatarUrl == newNote.avatarUrl
    }
}
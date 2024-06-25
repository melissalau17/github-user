package com.dicoding.githubuser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.databinding.ItemUserBinding

class FavoriteUserAdapter : ListAdapter<FavoriteUser, FavoriteUserAdapter.UserViewHolder>(
    DIFF_CALLBACK
) {

    private var onItemSelected: (FavoriteUser) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onItemSelected)
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUser, onItemSelected: (FavoriteUser) -> Unit) {
            binding.apply {
                Glide.with(binding.profileImage.context)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(binding.profileImage)

                tvUsername.text = user.username

                root.setOnClickListener {
                    onItemSelected(user)
                }
            }
        }
    }

    fun setOnItemSelected(onItemSelected: (FavoriteUser) -> Unit) {
        this.onItemSelected = onItemSelected
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.username == newItem.username // Assuming id is a unique identifier for users
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}
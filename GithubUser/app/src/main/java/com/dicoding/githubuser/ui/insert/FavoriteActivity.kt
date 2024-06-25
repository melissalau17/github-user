package com.dicoding.githubuser.ui.insert

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.ui.UserDetailActivity
import com.dicoding.githubuser.ui.ViewModelFactory
import com.dicoding.githubuser.ui.main.FavoriteUserAdapter

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteUserAdapter: FavoriteUserAdapter
    private lateinit var viewModel: FavoriteAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUserAdapter = FavoriteUserAdapter() // Initialize with empty list
        favoriteUserAdapter.setOnItemSelected {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra(Intent.EXTRA_USER, it.username)
            startActivity(intent)
        }

        binding.idFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = favoriteUserAdapter
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory).get(FavoriteAddUpdateViewModel::class.java)
        viewModel.getAllFavoriteUsers().observe(this) { favoriteUsers ->
            favoriteUserAdapter.submitList(favoriteUsers)
        }
    }
}
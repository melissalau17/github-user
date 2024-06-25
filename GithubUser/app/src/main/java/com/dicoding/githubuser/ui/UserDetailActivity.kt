package com.dicoding.githubuser.ui

import DetailViewModel
import FollowFragment
import SectionPageAdapter
import android.content.Intent.EXTRA_USER
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var isFavorite: Boolean = false
    private lateinit var favoriteUser: FavoriteUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        // Retrieve user detail data
        val detailUser = intent.getStringExtra(EXTRA_USER)
        detailUser?.let {
            FollowFragment.username = it
            viewModel.getDetailUser(it)
            viewModel.getFavoriteByUsername(it).observe(this) { favoriteUser ->
                if (favoriteUser != null) {
                    isFavorite = true
                    binding.floatingActionButton.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    isFavorite = false
                    binding.floatingActionButton.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }

        }

        // Observe user detail data
        viewModel.userDetail.observe(this) { userDetail ->
            userDetail?.let {
                setDetailUser(userDetail)
                if (!::favoriteUser.isInitialized){
                    favoriteUser = FavoriteUser(
                        username = userDetail.login.toString(),
                        avatarUrl = userDetail.avatarUrl
                    )
                }
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.floatingActionButton.setOnClickListener {
            if (isFavorite) {
                viewModel.delete(favoriteUser)
                isFavorite = false
                binding.floatingActionButton.setImageResource(R.drawable.baseline_favorite_border_24)
            } else {
                viewModel.insert(favoriteUser)
                isFavorite = true
                binding.floatingActionButton.setImageResource(R.drawable.baseline_favorite_24)
            }
        }

        // Set up ViewPager and TabLayoutMediator
        val sectionPageAdapter = SectionPageAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionPageAdapter
        val TAB_TITLES = resources.getStringArray(R.array.tab_titles)
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = TAB_TITLES.getOrNull(position) ?: "Default Title"
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setDetailUser(user: DetailUserResponse) {
        // Update UI with user detail data
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            Glide.with(this@UserDetailActivity)
                .load(user.avatarUrl)
                .into(profileImage)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading indicator based on isLoading value
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

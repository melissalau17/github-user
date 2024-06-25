package com.dicoding.githubuser.ui.main

import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.model.MainViewModel
import com.dicoding.githubuser.ui.UserAdapter
import com.dicoding.githubuser.ui.UserDetailActivity
import com.dicoding.githubuser.ui.ViewModelFactory
import com.dicoding.githubuser.ui.insert.FavoriteActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : UserAdapter
    private var searchType: String = "default" // Define searchType
    private var isDarkMode: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
        val mainViewModel = ViewModelProvider(this, factory).get(
            MainViewModel::class.java
        )
        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.floatingActionButtonDarkMode.setImageResource(R.drawable.baseline_brightness_high_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.floatingActionButtonDarkMode.setImageResource(R.drawable.baseline_dark_mode_24)
            }
            isDarkMode = isDarkModeActive
        }

        binding.floatingActionButtonDarkMode.setOnClickListener {
            mainViewModel.saveThemeSetting(!isDarkMode)
        }

        supportActionBar?.hide()

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set up RecyclerView
        adapter = UserAdapter()
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        adapter.setOnItemSelected {
            // Handle item click
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra(EXTRA_USER, it.login)
            startActivity(intent)
        }

        // Observe the user data
        viewModel.user.observe(this) {
            adapter.submitList(it.items)
        }

        // Observe the loading state
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        // Set up search action
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { textView, actionId, even ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            val query = binding.searchView.editText.text.toString().trim()
            searchUsers(query, searchType)
            false
        }

        // Open Favorite Page
        binding.floatingActionButtonFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchUsers(query: String, searchType: String) {
        // Perform the search operation
        viewModel.searchUsers(query)

        // Hide keyboard if it's open
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.editText.windowToken, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading indicator based on isLoading value
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
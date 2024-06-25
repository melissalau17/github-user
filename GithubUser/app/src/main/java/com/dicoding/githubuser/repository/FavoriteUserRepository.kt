package com.dicoding.githubuser.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.SettingPreferences
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.database.FavoriteUserDao
import com.dicoding.githubuser.database.FavoriteUserDatabase
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val dataStore: SettingPreferences
    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
        dataStore = SettingPreferences.getInstance(application.dataStore)
    }
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUsers()
    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.update(favoriteUser) }
    }
    fun getFavoriteByUsername(favoriteUser: String): LiveData<FavoriteUser> {
        return mFavoriteUserDao.getFavoriteByUsername(favoriteUser)
    }

    fun getThemeSetting(): Flow<Boolean> = dataStore.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        dataStore.saveThemeSetting(isDarkModeActive)
}
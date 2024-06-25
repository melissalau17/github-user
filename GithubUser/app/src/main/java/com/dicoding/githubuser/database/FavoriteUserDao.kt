package com.dicoding.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)
    @Update
    fun update(favoriteUser: FavoriteUser)
    @Delete
    fun delete(favoriteUser: FavoriteUser)
    @Query("SELECT * from FavoriteUser ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>
    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteByUsername(username: String): LiveData<FavoriteUser>

}
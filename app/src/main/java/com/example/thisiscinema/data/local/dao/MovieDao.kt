package com.example.thisiscinema.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thisiscinema.data.local.enitity.MovieEntity

@Dao
interface MovieDao {
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    @JvmSuppressWildcards
@Query("SELECT * FROM movie_tbl")
    suspend fun getMovies():  List<MovieEntity>
}
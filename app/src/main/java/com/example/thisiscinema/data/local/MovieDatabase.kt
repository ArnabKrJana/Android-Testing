package com.example.thisiscinema.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thisiscinema.data.local.dao.MovieDao
import com.example.thisiscinema.data.local.enitity.MovieEntity



@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}


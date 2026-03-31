package com.example.thisiscinema.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thisiscinema.data.local.MovieDatabase
import com.example.thisiscinema.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            name = "Movie_Database"
        ).build()
    }

    @Singleton
    @Provides
    fun providesDao(db: MovieDatabase): MovieDao = db.movieDao
}
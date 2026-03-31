package com.example.thisiscinema.data.local.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_tbl")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val releaseDate: String,
    val rating: String,
    val poster: String
)

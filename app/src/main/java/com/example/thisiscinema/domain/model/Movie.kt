package com.example.thisiscinema.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val genre: String= "various",
    val rating: String,
    val poster: String
)


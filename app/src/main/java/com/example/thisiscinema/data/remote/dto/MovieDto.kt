package com.example.thisiscinema.data.remote.dto

data class MovieDto(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
package com.example.thisiscinema.domain.repository

import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<Resource<List<Movie>>>
}



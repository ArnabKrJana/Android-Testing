package com.example.thisiscinema.data.remote

import com.example.thisiscinema.data.remote.dto.MovieDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    //base_url=https://api.themoviedb.org/3/movie

    @GET("now_playing")
    suspend fun getMovies(): Response<MovieDto>

}
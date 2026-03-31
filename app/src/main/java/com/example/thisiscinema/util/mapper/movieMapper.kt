package com.example.thisiscinema.util.mapper

import com.example.thisiscinema.data.local.enitity.MovieEntity
import com.example.thisiscinema.data.remote.dto.MovieDto
import com.example.thisiscinema.data.remote.dto.Result
import com.example.thisiscinema.domain.model.Movie

fun Result.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        releaseDate = this.release_date,
        rating = this.vote_average.toString(),
        poster = "https://image.tmdb.org/t/p/w500${this.poster_path}",
        genre = this.genre_ids.joinToString(", ")
    )
}

fun MovieDto.toMovieList(): List<Movie> {
    return this.results.map { it.toMovie() }
}


fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate,
        rating = this.rating,
        poster = this.poster
    )
}

fun Result.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        releaseDate = this.release_date,
        rating = this.vote_average.toString(),
        poster = "https://image.tmdb.org/t/p/w500${this.poster_path}"
    )
}

fun MovieDto.toMovieEntityList(): List<MovieEntity> {
    return this.results.map { it.toMovieEntity() }
}

package com.example.thisiscinema.util.mapper

import com.example.thisiscinema.data.local.enitity.MovieEntity
import com.example.thisiscinema.data.remote.dto.Result
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ToMovieTest {
    private lateinit var fakeNetworkResult: Result
    private lateinit var fakeMovieEntity: MovieEntity

    @Before
    fun setUp() {
        fakeNetworkResult = Result(
            adult = false,
            backdrop_path = "/backdrop.jpg",
            genre_ids = listOf(28, 12),
            id = 555,
            original_language = "en",
            original_title = "Action Movie",
            overview = "Explosions and stuff.",
            popularity = 100.5,
            poster_path = "/poster123.jpg",
            release_date = "2024-01-01",
            title = "Action Movie",
            video = false,
            vote_average = 8.5,
            vote_count = 200
        )
        fakeMovieEntity=MovieEntity(
            id = 10,
            title = "Local Movie",
            releaseDate = "2023",
            rating = "9.0",
            poster = "full_url_here"
        )

    }


    @Test
    fun `Result toMovie maps fields correctly and appends image base URL`() {
        val mappedMovie = fakeNetworkResult.toMovie()
        assertEquals("Action Movie", mappedMovie.title)
        assertEquals("https://image.tmdb.org/t/p/w500/poster123.jpg", mappedMovie.poster)

    }

    @Test
    fun `MovieEntity toMovie maps database fields correctly`() {
        val mappedMovie=fakeMovieEntity.toMovie()
        assertEquals("full_url_here",mappedMovie.poster)
    }


}
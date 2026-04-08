package com.example.thisiscinema.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.thisiscinema.data.local.MovieDatabase
import com.example.thisiscinema.data.local.enitity.MovieEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var movieDao: MovieDao
    private lateinit var movieDatabase: MovieDatabase

    @Before
    fun setUp() {
        //Initialize the In-Memory database
        movieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        )
            .allowMainThreadQueries() // to run queries in main/ui thread
            .build()

        // extracting dao
        movieDao = movieDatabase.movieDao
    }

    @After
    fun tearDown() {
        movieDatabase.close() // to prevent memory leaks
    }

    @Test
    fun insertMovies_and_readThem_returnsCorrectData() = runTest {
        //Arrange
        val movie1 = MovieEntity(
            id = 1,
            title = "Inception",
            releaseDate = "2010-07-16",
            rating = "8.8",
            poster = "/inception.jpg"
        )
        val movie2 = MovieEntity(
            id = 2,
            title = "Interstellar",
            releaseDate = "2014-11-07",
            rating = "8.6",
            poster = "/interstellar.jpg"
        )
        val listOfMovieEntity = listOf(
            movie1, movie2
        )
        //Act
        movieDao.insertMovies(listOfMovieEntity)
        val movies=movieDao.getMovies()
        //Assert
        assertEquals(2,movies.size)
        assertEquals(movie1,movies[0])
        assertEquals(movie2,movies[1])
    }


//    @Test
//    fun getMovies() {
//    }

}
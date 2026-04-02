package com.example.thisiscinema.data.repoImpl

import app.cash.turbine.test
import com.example.thisiscinema.data.local.MovieDatabase
import com.example.thisiscinema.data.local.dao.MovieDao
import com.example.thisiscinema.data.local.enitity.MovieEntity
import com.example.thisiscinema.data.remote.MovieService
import com.example.thisiscinema.data.remote.dto.Dates
import com.example.thisiscinema.data.remote.dto.MovieDto
import com.example.thisiscinema.data.remote.dto.Result
import com.example.thisiscinema.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.util.Date

class MovieRepositoryImplTest {
    private lateinit var repository: MovieRepositoryImpl // what I am testing here, The Repository

    private lateinit var service: MovieService
    private lateinit var db: MovieDatabase
    private lateinit var dao: MovieDao

    // The Coroutine Dispatcher
    private lateinit var testDispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        //mocked all the dependencies
        service = mockk()
        db = mockk()
        dao = mockk()
        //The Coroutine Test Dispatcher
        testDispatcher = StandardTestDispatcher()

        """ 
        simulating this behavior private val dao = db.movieDao ie. 
        when someone call db.dao then it should return my mocked dao for testing 
        
        """.trimIndent()
        every { db.movieDao } returns dao

        //Instantiate the repository MovieRepositoryImpl() with mock dependencies
        repository = MovieRepositoryImpl(
            service = service,
            db = db,
            ioDispatcher = testDispatcher
        )
    }


    @Test
    fun `getMovies emits Loading then Success when API call is successful`() =
        runTest(testDispatcher) {
//fun getMovies() : Flow<Resource<List<Movie>>> for Resource.Success(successData)
            """
            1. find on database first
            2. emit loading state
            3. try to call service for remote data fetch
            4. if successful -->
                    insert movies into database
            5. get movies from database
            6. emit success state with movies
        """.trimIndent()
            //Arrange
            //fake data for database
            val fakeEntity = MovieEntity(
                id = 1,
                title = "Dune 3",
                releaseDate = "18th Dec 2026",
                rating = "8.79",
                poster = "url"
            )
            val fakeUpdatedEntity = MovieEntity(
                id = 1,
                title = "Perfect Days",
                releaseDate = "18th Dec 2023",
                rating = "8.79",
                poster = "url"
            )
            val fakeResult = Result(
                adult = false,
                backdrop_path = "",
                genre_ids = listOf(1),
                id = 1,
                original_language = "en",
                original_title = "Perfect Days",
                overview = "",
                popularity = 1.0,
                poster_path = "url",
                release_date = "18th Dec 2023",
                title = "Perfect Days",
                video = false,
                vote_average = 8.79,
                vote_count = 100
            )
            //fake data for service
            val fakeDto = MovieDto(
                dates = Dates("", ""),
                page = 1,
                results = listOf(fakeResult),
                total_pages = 1,
                total_results = 1
            )

            // Mock the DAO to return our fake local data
            coEvery { dao.getMovies() } returns listOf(fakeEntity) andThen listOf(fakeUpdatedEntity)
            // Mock the Service to return a successful Retrofit response
            coEvery { service.getMovies() } returns Response.success(fakeDto)
            // Mock the DAO insertion to just successfully run without doing anything
            coEvery { dao.insertMovies(any()) } just runs
//Act & Assert --> Collect The Flow or Observe

            repository.getMovies().test {
                val loading = awaitItem()
                assertTrue(loading is Resource.Loading)
                assertEquals("Dune 3", loading.data.orEmpty().first().title)

                val success = awaitItem()
                assertTrue(success is Resource.Success)
                assertEquals("Perfect Days", success.data.orEmpty().first().title)
                cancelAndIgnoreRemainingEvents()
            }


            coVerify(exactly = 1) { dao.insertMovies(any()) }

        }


    @Test
    fun `getMovies emits Loading then Error when API throws IOException`() =
        runTest(testDispatcher) {
            // Arrange
            val fakeLocalEntity = MovieEntity(
                id = 1, title = "Cached Movie", releaseDate = "2023", rating = "8.0", poster = "url"
            )
            val expectedErrorMessage = "Please check your internet connection."

            // The DAO just returns the cached data.
            coEvery { dao.getMovies() } returns listOf(fakeLocalEntity)

            // Mock the service to completely fail and throw an exception (No Internet)
            coEvery { service.getMovies() } throws IOException()

            // Act & Assert
            repository.getMovies().test {
                // First emission: Loading state with the old, cached data
                val loadingState = awaitItem()
                assertTrue(loadingState is Resource.Loading)
                assertEquals("Cached Movie", loadingState.data?.first()?.title)

                // Second emission: Error state, but STILL containing the cached data
                val errorState = awaitItem()
                assertTrue(errorState is Resource.Error)
                assertEquals(expectedErrorMessage, errorState.message)
                assertEquals("Cached Movie", errorState.data?.first()?.title)

                cancelAndIgnoreRemainingEvents()
            }

            // Verify: Ensure we NEVER tried to insert anything into the database!
            coVerify(exactly = 0) { dao.insertMovies(any()) }
        }
}


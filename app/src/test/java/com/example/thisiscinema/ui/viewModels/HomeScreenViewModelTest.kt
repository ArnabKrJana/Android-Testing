package com.example.thisiscinema.ui.viewModels

import app.cash.turbine.test
import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.domain.repository.MovieRepository
import com.example.thisiscinema.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var movieRepository: MovieRepository

    @Before
    fun setUp() {
        movieRepository = mockk()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchMovies_emits_SuccessState() {
        return runTest {
            //Arrange
            val fakeMovies = listOf(
                Movie(1, "Test Movie", "2024", "Drama", "9.0", "url")
            )
            coEvery {
                movieRepository.getMovies()
            } returns flow {
                emit(Resource.Loading())
                emit(Resource.Success(fakeMovies))
            }

            //Act
            homeScreenViewModel = HomeScreenViewModel(movieRepository)
            //Assert
            homeScreenViewModel.movieState.test {
                val loading = awaitItem()
                val success = awaitItem()
                assert(loading is Resource.Loading)
                assert(success is Resource.Success)
                assertEquals(1, success.data?.size)
                cancelAndIgnoreRemainingEvents()
            }
        }

    }

    @Test
    fun fetchMovies_emits_ErrorState() {
        return runTest {
            //Arrange
            val errorMessage = "Something went wrong"
            coEvery { movieRepository.getMovies() } returns flow {
                emit(Resource.Loading())
                emit(Resource.Error(errorMessage))
            }
            //Act
            homeScreenViewModel = HomeScreenViewModel(movieRepository)
            //Assert
            homeScreenViewModel.movieState.test {
                val loading = awaitItem()
                val error = awaitItem()
                assert(loading is Resource.Loading)
                assert(error is Resource.Error)
                assertEquals(errorMessage, error.message)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

}
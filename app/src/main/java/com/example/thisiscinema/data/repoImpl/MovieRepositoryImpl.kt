package com.example.thisiscinema.data.repoImpl

import com.example.thisiscinema.data.local.MovieDatabase
import com.example.thisiscinema.data.remote.MovieService
import com.example.thisiscinema.di.IoDispatchersQualifier
import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.domain.repository.MovieRepository
import com.example.thisiscinema.util.Resource
import com.example.thisiscinema.util.mapper.toMovie
import com.example.thisiscinema.util.mapper.toMovieEntityList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService,
    private val db: MovieDatabase,
   @IoDispatchersQualifier private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    private val dao = db.movieDao

    override suspend fun getMovies(): Flow<Resource<List<Movie>>> = flow {

        val localMovies = dao.getMovies().map { it.toMovie() }

        emit(Resource.Loading(data = localMovies))

        try {
            val response = service.getMovies()

            if (response.isSuccessful) {
                response.body()?.let { movieDto ->
                    dao.insertMovies(movieDto.toMovieEntityList())
                }
            } else {
                emit(Resource.Error(message = "Server Error: ${response.code()}", data = localMovies))
                return@flow
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Something went wrong with the server.", data = localMovies))
            return@flow
        } catch (e: IOException) {
            emit(Resource.Error(message = "Please check your internet connection.", data = localMovies))
            return@flow
        } catch (e: Exception) {
            emit(Resource.Error(message = "An unexpected error occurred.", data = localMovies))
            return@flow
        }

        val updatedLocalMovies = dao.getMovies().map { it.toMovie() }
        emit(Resource.Success(data = updatedLocalMovies))

    }.flowOn(context = ioDispatcher)
}
package com.example.thisiscinema.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.ui.theme.ThisIsCinemaTheme
import com.example.thisiscinema.ui.uiComponents.MovieCardWithShimmer
import com.example.thisiscinema.util.Resource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    movieState: Resource<List<Movie>>,
    onRetry: () -> Unit
) {
    Column(modifier.fillMaxSize()) {

        val movies = movieState.data ?: emptyList()


        if (movieState is Resource.Error && movies.isNotEmpty()) {
            Text(
                text = "Offline Mode: ${movieState.message}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (movieState is Resource.Loading && movies.isEmpty()) {
            LazyRow(modifier = modifier.fillMaxWidth()
                .testTag("shimmer_list")

            ) {
                items(5) {
                    MovieCardWithShimmer(
                        modifier = modifier.padding(8.dp).width(225.dp).aspectRatio(0.7f).testTag("shimmer_card"),
                        movie = null,
                        isLoading = true
                    )
                }
            }
        } else if (movies.isNotEmpty()) {
            LazyRow(modifier = modifier.fillMaxWidth().testTag("movie_list")) {
                items(movies.size, key = { movies[it].id }) { index ->
                    MovieCardWithShimmer(
                        modifier = modifier.padding(8.dp).width(225.dp).aspectRatio(0.7f).testTag("movie_card"),
                        movie = movies[index],
                        isLoading = false
                    )
                }
            }
        } else if (movieState is Resource.Error && movies.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = movieState.message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleMovies = listOf(
        Movie(
            id = 1,
            title = "Inception",
            releaseDate = "2010-07-16",
            genre = "Sci-Fi",
            rating = "8.8",
            poster = ""
        ),
        Movie(
            id = 2,
            title = "The Dark Knight",
            releaseDate = "2008-07-18",
            genre = "Action",
            rating = "9.0",
            poster = ""
        ),
        Movie(
            id = 3,
            title = "Interstellar",
            releaseDate = "2014-11-07",
            genre = "Adventure",
            rating = "8.6",
            poster = ""
        )
    )
    ThisIsCinemaTheme {
        HomeScreen(
            movieState = Resource.Success(sampleMovies),
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLoadingPreview() {
    ThisIsCinemaTheme {
        HomeScreen(
            movieState = Resource.Loading(),
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenErrorPreview() {
    ThisIsCinemaTheme {
        HomeScreen(
            movieState = Resource.Error("Failed to fetch movies"),
            onRetry = {}
        )
    }
}

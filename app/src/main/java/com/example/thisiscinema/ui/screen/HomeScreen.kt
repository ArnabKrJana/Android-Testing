package com.example.thisiscinema.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.util.Resource
import com.example.thisiscinema.ui.uiComponents.MovieCardWithShimmer

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
            LazyRow(modifier = modifier.fillMaxWidth()) {
                items(5) {
                    MovieCardWithShimmer(
                        modifier = modifier.padding(8.dp).width(225.dp).aspectRatio(0.7f),
                        movie = null,
                        isLoading = true
                    )
                }
            }
        } else if (movies.isNotEmpty()) {
            LazyRow(modifier = modifier.fillMaxWidth()) {
                items(movies.size, key = { movies[it].id }) { index ->
                    MovieCardWithShimmer(
                        modifier = modifier.padding(8.dp).width(225.dp).aspectRatio(0.7f),
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
                    text = movieState.message ?: "An unknown error occurred",
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
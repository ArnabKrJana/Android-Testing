package com.example.thisiscinema.ui.uiComponents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.thisiscinema.R
import com.example.thisiscinema.domain.model.Movie
import com.example.treasure.ui.uiComponents.AnimatedShimmer

@Composable
fun MovieCardWithShimmer(
    modifier: Modifier,
    movie: Movie?,
    isLoading: Boolean
) {
    if (isLoading) {
        AnimatedShimmer(modifier = modifier)
    } else {
        movie?.let {
            MovieCard(modifier = modifier, movie = it)
        }
    }
}
@Composable
fun MovieCard(modifier: Modifier, movie: Movie) {
val gradient= listOf(Color.Transparent,Color.Black)
    val brush= Brush.verticalGradient(gradient)

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(8)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = movie.poster,
                contentDescription = "thumbnail",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier

                    .fillMaxWidth()
                    .background(brush = brush)
                    .padding(vertical = 16.dp, horizontal = 12.dp)
                    .align(alignment = Alignment.BottomEnd)
//                contentAlignment = Alignment.TopEnd
            )
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
//                    Badge(
//                        containerColor = MaterialTheme.colorScheme.onBackground,
//                        contentColor = MaterialTheme.colorScheme.background
//                    ) {
//                        Text(
//                            text = movie.genre,
//                            style = MaterialTheme.typography.headlineSmall,
////                            color = MaterialTheme.colorScheme.background,
//                            fontWeight = FontWeight.Normal,
//                            modifier = Modifier.padding(4.dp)
//                        )
//                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Badge(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.surfaceDim
                    ) {
                        Text(
                            text = movie.releaseDate,
                            style = MaterialTheme.typography.headlineSmall,
//                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Badge(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    ) {
                        Text(
                            text = movie.rating,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(4.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MovieCardLightPreview() {

    val movie = Movie(
        id = 2,
        title = "The Shawshank Redemption",
        releaseDate = "1994",
        genre = "Drama",
        rating = "9.3",
        poster = R.drawable.bg_0022.toString()
    )
    MaterialTheme {
        MovieCard(
            modifier = Modifier
                .padding(8.dp).width(250.dp)
                .aspectRatio(0.6f),
            movie = movie
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieCardDarkPreview() {
    val movie = Movie(
        id = 2,
        title = "The Shawshank Redemption",
        releaseDate = "1994",
        genre = "Drama",
        rating = "9.3",
        poster = R.drawable.bg_0013.toString()
    )
    MaterialTheme() {
        MovieCardWithShimmer(
            modifier = Modifier
                .padding(8.dp).width(250.dp)
                .aspectRatio(0.6f),
            movie = movie,
            isLoading = true
        )
    }
}


package com.example.thisiscinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thisiscinema.ui.screen.HomeScreen
import com.example.thisiscinema.ui.theme.ThisIsCinemaTheme
import com.example.thisiscinema.ui.viewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThisIsCinemaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: HomeScreenViewModel= hiltViewModel()
                    val state=viewModel.movieState.collectAsStateWithLifecycle()
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        movieState = state.value,
                        onRetry = { viewModel.fetchMovies() }
                    )



                }
            }
        }
    }
}

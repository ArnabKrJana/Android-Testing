package com.example.thisiscinema.ui.screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.thisiscinema.domain.model.Movie
import com.example.thisiscinema.util.Resource
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    //  This rule is the magic engine that lets us test Compose UI
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorState_withNoLocalData_displaysErrorMessageAndRetryButton() {
//Arrange
        val errorMessage = "Please check your internet connection"
        val errorState = Resource.Error<List<Movie>>(
            message = errorMessage,
            data = emptyList()
        )
        var retryIsClicked = false

        //Act
        composeTestRule.setContent {
            HomeScreen(
                movieState = errorState
            ) {
                retryIsClicked = true
            }
        }
        // Assert: Verify the UI looks right and acts right
        //check if the exact error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        //check if the retry button is displayed
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed().performClick()
        //check the "Retry" button is actually functional by checking it is firing the lambda onRetry:()->Unit
        assertTrue(retryIsClicked)


    }


    @Test
    fun loadingState_withNoLocalData_displaysShimmerEffect_butNoRetryButton() {
        //Arrange
        val loadingState = Resource.Loading<List<Movie>>()

        //Act
        composeTestRule.setContent {
            HomeScreen(
                movieState = loadingState
            ) { }
        }

        //Assert
        /*
        to test a composable like shimmer effect I cant just call it 'cause it has no name or anything like that,
        in that case we have to tag that composable using 'testTag' in the modifier first
         */
        //  composeTestRule.onAllNodesWithTag("shimmer_card").assertCountEquals(5)
        /* it will fail due to LazyRow, 'cause it is lazy ,
         did not draw all the 5 composable since only 2 can fit into the screen */
        // composeTestRule.onNodeWithText("Retry").assertDoesNotExist()
        /*
        to successfully test it for all the device we have to tell the compose to
         scroll to the end of the row and then count and evaluate
         */

        //verify the list itself is visible
        composeTestRule.onNodeWithTag("shimmer_list").assertIsDisplayed()
        //verify at least one of the shimmer composable is visible
        composeTestRule.onAllNodesWithTag("shimmer_card").onFirst().assertIsDisplayed()
        // now scroll to the end of the list [0 to 4]
        // that means if the test fails that mean I don't have 5 composable in the list
        composeTestRule.onNodeWithTag("shimmer_list").performScrollToIndex(4)
        //Retry button should not appear here
        composeTestRule.onNodeWithText("Retry").assertDoesNotExist()
    }

    @Test
    fun successState_withData_displaysListOfMovies() {
        //Arrange
        val sampleMovies = listOf(
            Movie(
                id = 1,
                title = "Inception",
                releaseDate = "2010-07-16",
                genre = "Sci-Fi",
                rating = "8.3",
                poster = "https://posterurl.com/someRandomId"
            ),
            Movie(
                id = 2,
                title = "Notebook",
                releaseDate = "2010-07-16",
                genre = "Romance",
                rating = "7.8",
                poster = "https://posterurl.com/someRandomId"
            ),
            Movie(
                id = 3,
                title = "Civil War",
                releaseDate = "2010-07-16",
                genre = "War",
                rating = "8.9",
                poster = "https://posterurl.com/someRandomId"
            ),
            Movie(
                id = 4,
                title = "Mad max",
                releaseDate = "2010-07-16",
                genre = "Action",
                rating = "8.0",
                poster = "https://posterurl.com/someRandomId"
            ),
            Movie(
                id = 5,
                title = "Arrival",
                releaseDate = "2010-07-16",
                genre = "Sci-Fi",
                rating = "8.1",
                poster = "https://posterurl.com/someRandomId"
            ),
            Movie(
                id = 6,
                title = "Interstellar",
                releaseDate = "2010-07-16",
                genre = "Sci-Fi",
                rating = "8.7",
                poster = "https://posterurl.com/someRandomId"
            )
        )
        val successState = Resource.Success(sampleMovies)

        //Act
        composeTestRule.setContent {
            HomeScreen(
                movieState = successState
            ) { }
        }
        //Assert
        composeTestRule.onNodeWithTag("movie_list").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("movie_card").onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        // since all the card is not going to fit inside screen so we have to scroll to the end of the list. But this time
        // I want to check movie card by movie name is displayed or  by scrolling to that movie
        composeTestRule.onNodeWithTag("movie_list").performScrollToNode(hasText("Arrival"))

        composeTestRule.onNodeWithTag("shimmer_list").assertDoesNotExist()
        composeTestRule.onNodeWithText("Retry").assertDoesNotExist()

    }
}
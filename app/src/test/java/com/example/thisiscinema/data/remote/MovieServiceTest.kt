package com.example.thisiscinema.data.remote

import com.example.thisiscinema.data.remote.dto.MovieDto
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.HttpURLConnection

class MovieServiceTest {

    private lateinit var apiService: MovieService
    private lateinit var mockWebServer: MockWebServer
    @Before
    fun setUp() {
      //start the fake server
        mockWebServer= MockWebServer()
        mockWebServer.start()

        //create Retrofit Instance
        val retrofit= Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //create the api service
        apiService=retrofit.create(MovieService::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getMovies parses successful JSON response correctly to Dto`() = runTest {
        // Arrange: Create a raw JSON string that looks exactly like the real TMDB API response
        val jsonResponse = """
            {
              "dates": { "maximum": "2024-05-01", "minimum": "2024-04-01" },
              "page": 1,
              "results": [
                {
                  "id": 101,
                  "title": "Dune: Part Two",
                  "release_date": "2024-02-27",
                  "vote_average": 8.5
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()
val expectedResponse= MockResponse()
    .setResponseCode(HttpURLConnection.HTTP_OK)
    .setBody(jsonResponse)
    .addHeader("Content-Type","application/json")

        //enqueue the response to send
        mockWebServer.enqueue(expectedResponse)

        //Act: Actual Retrofit call to the MockWebServer
        val response=apiService.getMovies()

        // Assert: Verify Retrofit successfully converted the JSON into our Kotlin Data Classes
        assertTrue(response.isSuccessful)
        val body = response.body()

        // Check if the data matches the JSON string
        assertEquals(1, body?.page)
        assertEquals(1, body?.results?.size)
        assertEquals("Dune: Part Two", body?.results?.first()?.title)
        assertEquals(101, body?.results?.first()?.id)

    }

    @Test
    fun `getMovies handles server error correctly`() = runTest {
        // Arrange: Tell the fake server to return a 404 Not Found
        val errorResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockWebServer.enqueue(errorResponse)

        // Act
        val response = apiService.getMovies()

        // Assert
        assertTrue(!response.isSuccessful)
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.code())
    }
}
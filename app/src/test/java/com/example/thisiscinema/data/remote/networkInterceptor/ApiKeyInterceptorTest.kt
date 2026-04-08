package com.example.thisiscinema.data.remote.networkInterceptor

import com.example.thisiscinema.util.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class ApiKeyInterceptorTest {
    private lateinit var apiKeyInterceptor: ApiKeyInterceptor
    @Before
    fun setUp() {
      apiKeyInterceptor= ApiKeyInterceptor()
    }



    @Test
    fun `intercept appends TMDB API key to the original request URL`() {
        //Arrange
        //creating a fake request with a url
        val originalUrl = "https://api.themoviedb.org/3/movie/now_playing"
        val originalRequest = Request.Builder().url(originalUrl).build()
//Mock the OkHttp Chain
        val chain=mockk<Interceptor.Chain>()
        every { chain.request() } returns originalRequest

        // Create a 'slot' to catch the new request mid-air
        val interceptedRequestSlot = slot<Request>()

        // When the interceptor tells the chain to 'proceed', we CAPTURE the request it passes in!
        every { chain.proceed(capture(interceptedRequestSlot) )} returns Response.Builder()
            .request(originalRequest) // Just returning dummy data so the function doesn't crash
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
        //Act
        apiKeyInterceptor.intercept(chain)
        //Assert
        val capturedRequest = interceptedRequestSlot.captured
        val capturedUrl = capturedRequest.url
        val apiKeyParameter = capturedUrl.queryParameter("api_key")
        assertEquals(Constants.TMDB_API_KEY, apiKeyParameter)

        // Verify it didn't mess up the rest of our URL
        assertEquals("api.themoviedb.org", capturedUrl.host)
        assertEquals("/3/movie/now_playing", capturedUrl.encodedPath)
    }

}
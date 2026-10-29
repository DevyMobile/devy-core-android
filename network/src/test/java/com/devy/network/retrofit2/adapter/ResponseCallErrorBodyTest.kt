package com.devy.network.retrofit2.adapter

import com.devy.network.retrofit2.response.ApiResponse
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ResponseCallTestApi {
    @GET("test")
    fun get(): Call<ApiResponse<String>>
}

class ResponseCallErrorBodyTest {
    private lateinit var server: MockWebServer
    private lateinit var api: ResponseCallTestApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .addCallAdapterFactory(ResponseCallAdapter.Factory())
            .build()
            .create(ResponseCallTestApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun request(code: Int, body: String): ApiResponse<String> {
        server.enqueue(MockResponse().setResponseCode(code).setBody(body))
        val response = api.get().execute().body()
        assertNotNull(response)
        return response!!
    }

    @Test
    fun htmlErrorBody_isHttpErrorWithStatusCode() {
        val result = request(413, "<html><body>RequestEntityTooLarge</body></html>")

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(413, result.statusCode)
        assertEquals(413, result.error?.code)
        assertEquals(result.statusMessage, result.error?.message)
        assertTrue((result.error?.detail?.get("body") as String).contains("RequestEntityTooLarge"))
    }

    @Test
    fun plainTextErrorBody_isHttpErrorWithStatusCode() {
        val result = request(502, "Bad Gateway")

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(502, result.statusCode)
        assertEquals(result.statusMessage, result.error?.message)
        assertEquals("Bad Gateway", result.error?.detail?.get("body"))
    }

    @Test
    fun emptyErrorBody_isHttpError() {
        val result = request(500, "")

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(500, result.statusCode)
        assertEquals(result.statusMessage, result.error?.message)
        assertEquals(null, result.error?.detail)
    }

    @Test
    fun jsonNullLiteral_usesStatusMessage() {
        val result = request(500, "null")

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(result.statusMessage, result.error?.message)
    }

    @Test
    fun jsonWithUnexpectedShape_isHttpError() {
        val result = request(500, """{"error":"not an object"}""")

        assertTrue(result is ApiResponse.Failure.HttpError)
        assertEquals(500, (result as ApiResponse.Failure.HttpError).statusCode)
    }

    @Test
    fun longErrorBody_isTruncated() {
        val result = request(500, "x".repeat(5_000))

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(500, (result.error?.detail?.get("body") as String).length)
    }

    @Test
    fun structuredJsonErrorBody_isParsedAsBefore() {
        val result = request(400, """{"error":{"message":"bad request","code":"400"}}""")

        assertTrue(result is ApiResponse.Failure.HttpError)
        result as ApiResponse.Failure.HttpError
        assertEquals(400, result.statusCode)
        assertEquals("bad request", result.error?.message)
        assertEquals(400, result.error?.code)
    }

    @Test
    fun successBody_isUnchanged() {
        val result = request(200, "\"ok\"")

        assertTrue(result is ApiResponse.Success)
        assertEquals("ok", (result as ApiResponse.Success).data)
    }
}

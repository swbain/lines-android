package com.stephenbain.lines.api

import retrofit2.http.GET
import retrofit2.http.Query

interface LinesApiService {

    @GET("/latest.json")
    suspend fun getLatest(
        @Query("page") page: Int = 0,
        @Query("no_definitions") noDefinitions: Boolean = true
    ): GetLatestApiResponse

}
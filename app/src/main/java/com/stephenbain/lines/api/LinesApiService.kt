package com.stephenbain.lines.api

import retrofit2.http.GET

interface LinesApiService {

    @GET("/latest.json")
    suspend fun getLatest(): GetLatestApiResponse

}
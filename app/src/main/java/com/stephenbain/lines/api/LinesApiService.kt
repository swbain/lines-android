package com.stephenbain.lines.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LinesApiService {

    @GET("/latest.json")
    suspend fun getLatest(
        @Query("page") page: Int = 0,
        @Query("no_definitions") noDefinitions: Boolean = true
    ): GetLatestApiResponse

    @GET("/c/{category}/{id}/l/latest.json")
    suspend fun getLatestForCategory(
        @Path("category") categoryName: String,
        @Path("id") id: Long,
        @Query("page") page: Int,
        @Query("order") order: String = "default",
        @Query("ascending") ascending: Boolean = false
    ): GetLatestApiResponse

    @GET("/categories.json")
    suspend fun getCategories(): CategoriesResponse

}

suspend fun LinesApiService.getLatestForCategory(category: Category, page: Int = 0): GetLatestApiResponse {
    return getLatestForCategory(category.name, category.id, page)
}
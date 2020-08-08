package com.stephenbain.lines.db

import com.stephenbain.lines.api.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesDao {

    fun getCategories(): Flow<List<Category>>

    suspend fun addCategories(categories: List<Category>)

}
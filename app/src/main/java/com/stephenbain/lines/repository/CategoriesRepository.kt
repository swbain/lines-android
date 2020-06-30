package com.stephenbain.lines.repository

import com.stephenbain.lines.api.Category
import com.stephenbain.lines.api.LinesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val api: LinesApiService) {

    fun getCategories(): Flow<List<CategoryItem>> = flow {
        val apiResponse = api.getCategories().categoryList.categories
        val items = mutableListOf<CategoryItem>(CategoryItem.AllCategories).apply {
            addAll(apiResponse.map { CategoryItem.SelectedCategory(it) })
        }
        emit(items)
    }

}
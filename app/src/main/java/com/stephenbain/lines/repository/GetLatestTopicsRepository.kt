package com.stephenbain.lines.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.stephenbain.lines.api.Category
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.api.getLatestForCategory
import timber.log.Timber
import javax.inject.Inject

class GetLatestTopicsRepository @Inject constructor(private val api: LinesApiService) {

    fun getTopics(categoryItem: CategoryItem) = Pager(config = PagingConfig(pageSize = 30, prefetchDistance = 5)) {
        GetLatestApiPagingSource(api, categoryItem)
    }.flow
}

private class GetLatestApiPagingSource(
    private val api: LinesApiService,
    private val categoryItem: CategoryItem
) : PagingSource<Int, Topic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        return try {
            val pageNumber = params.key ?: 0

            val response = when (categoryItem) {
                CategoryItem.AllCategories -> api.getLatest(page = pageNumber)
                is CategoryItem.SelectedCategory -> api.getLatestForCategory(
                    category = categoryItem.category,
                    page = pageNumber
                )
            }

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val hasNext = response.topicList.topics.isNotEmpty() && response.topicList.moreTopicsUrl != null
            val nextKey = if (hasNext) pageNumber + 1 else null
            LoadResult.Page(
                data = response.topicList.topics,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (t: Throwable) {
            Timber.e(t, "error loading home data")
            LoadResult.Error(t)
        }
    }
}
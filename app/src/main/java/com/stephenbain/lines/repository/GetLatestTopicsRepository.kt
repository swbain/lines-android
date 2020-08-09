package com.stephenbain.lines.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.stephenbain.lines.api.*
import com.stephenbain.lines.common.toHashMap
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetLatestTopicsRepository @Inject constructor(private val api: LinesApiService) {

    fun getTopics(categoryItem: CategoryItem): Flow<PagingData<TopicWithUsersAndCategory>> {
        return Pager(config = PagingConfig(pageSize = 30, prefetchDistance = 5)) {
            GetLatestApiPagingSource(api, categoryItem)
        }.flow
    }
}

private class GetLatestApiPagingSource(
    private val api: LinesApiService,
    private val categoryItem: CategoryItem
) : PagingSource<Int, TopicWithUsersAndCategory>() {

    var categories = emptyMap<Long, Category>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicWithUsersAndCategory> {
        return try {
            val pageNumber = params.key ?: 0

            if (categories.isEmpty()) {
                categories = hashMapOf<Long, Category>().apply {
                    api.getCategories().categoryList.categories.forEach { category ->
                        put(category.id, category)
                    }
                }
            }

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
                data = response.toTopicsWithUsers(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (t: Throwable) {
            Timber.e(t, "error loading home data")
            LoadResult.Error(t)
        }
    }

    private fun GetLatestApiResponse.toTopicsWithUsers(): List<TopicWithUsersAndCategory> {
        val allUsers = users.toHashMap { it.id }
        return topicList.topics.map { it.withUsers(allUsers) }
    }

    private fun Topic.withUsers(allUsers: Map<Long, User>): TopicWithUsersAndCategory {
        return TopicWithUsersAndCategory(
            topic = this,
            users = posters.mapNotNull { allUsers[it.userId] },
            category = categories[categoryId]
        )
    }
}

data class TopicWithUsersAndCategory(
    private val topic: Topic,
    val users: List<User>,
    val category: Category?
) : Topic by topic
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

    fun getTopics(): Flow<PagingData<TopicWithUsers>> {
        return Pager(config = PagingConfig(pageSize = 30, prefetchDistance = 5)) {
            GetLatestApiPagingSource(api)
        }.flow
    }
}

private class GetLatestApiPagingSource(
    private val api: LinesApiService
) : PagingSource<Int, TopicWithUsers>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicWithUsers> {
        return try {

            val pageNumber = params.key ?: 0

            val response = api.getLatest(page = pageNumber)

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

    private fun GetLatestApiResponse.toTopicsWithUsers(): List<TopicWithUsers> {
        val allUsers = users.toHashMap { it.id }
        return topicList.topics.map { it.withUsers(allUsers) }
    }

    private fun Topic.withUsers(allUsers: Map<Long, User>): TopicWithUsers {
        return TopicWithUsers(
            topic = this,
            users = posters.mapNotNull { allUsers[it.userId] }
        )
    }
}

data class TopicWithUsers(
    private val topic: Topic,
    val users: List<User>
) : Topic by topic
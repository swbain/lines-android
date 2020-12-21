package com.stephenbain.lines.repository

import androidx.paging.PagingSource
import com.stephenbain.lines.api.GetLatestApiResponse
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.Topic
import com.stephenbain.lines.api.User
import com.stephenbain.lines.common.toHashMap
import timber.log.Timber

class GetLatestApiPagingSource(private val api: LinesApiService) : PagingSource<Int, TopicWithUsers>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicWithUsers> {
        return try {

            val pageNumber = params.key ?: 0

            val response = api.getLatest(page = pageNumber)

            val hasNext = response.topicList.topics.size == response.topicList.perPage // if we have less topics than page size, we are at the last page.
            val nextKey = if (hasNext) pageNumber + 1 else null
            LoadResult.Page(
                data = response.toTopicsWithUsers(),
                prevKey = null, // only paging forward
                nextKey = nextKey
            )
        } catch (t: Throwable) {
            Timber.e(t, "error loading home data")
            LoadResult.Error(t)
        }
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

data class TopicWithUsers(
    private val topic: Topic,
    val users: List<User>
) : Topic by topic
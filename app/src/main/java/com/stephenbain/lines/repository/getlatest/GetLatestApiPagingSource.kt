package com.stephenbain.lines.repository.getlatest

import androidx.paging.PagingSource
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.Topic
import timber.log.Timber

class GetLatestApiPagingSource(private val api: LinesApiService) : PagingSource<Int, Topic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        return try {
            val pageNumber = params.key ?: 0
            Timber.d("requesting data load, home page %d", pageNumber)

            val response = api.getLatest(page = pageNumber)

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (response.topicList.topics.isNotEmpty()) pageNumber + 1 else null
            LoadResult.Page(data = response.topicList.topics, prevKey = prevKey, nextKey = nextKey)
        } catch (t: Throwable) {
            Timber.e(t, "error loading home data")
            LoadResult.Error(t)
        }
    }
}
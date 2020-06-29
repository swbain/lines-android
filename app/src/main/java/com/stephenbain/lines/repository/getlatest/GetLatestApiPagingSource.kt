package com.stephenbain.lines.repository.getlatest

import androidx.paging.PagingSource
import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.Topic
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.random.Random

class GetLatestApiPagingSource @Inject constructor(private val api: LinesApiService) : PagingSource<Int, Topic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        return try {
            val pageNumber = params.key ?: 0

            val response = api.getLatest(page = pageNumber)

            val random = Random.nextInt(0, 2)
            if (random == 1) {
                throw IllegalStateException("oops!")
            }

            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            val nextKey = if (response.topicList.topics.isNotEmpty()) pageNumber + 1 else null
            LoadResult.Page(data = response.topicList.topics, prevKey = prevKey, nextKey = nextKey)
        } catch (t: Throwable) {
            Timber.e(t, "error loading home data")
            LoadResult.Error(t)
        }
    }
}
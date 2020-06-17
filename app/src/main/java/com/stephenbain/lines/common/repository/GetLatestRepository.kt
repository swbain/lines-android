package com.stephenbain.lines.common.repository

import com.stephenbain.lines.common.api.LinesApiService
import com.stephenbain.lines.common.api.TopicJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestRepository @Inject constructor(private val apiService: LinesApiService) {

    fun getLatestTopics(): Flow<List<TopicJson>> {
        return flow {
            emit(apiService.getLatest().topic_list.topics)
        }
    }

}
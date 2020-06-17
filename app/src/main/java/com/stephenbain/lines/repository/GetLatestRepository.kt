package com.stephenbain.lines.repository

import com.stephenbain.lines.api.LinesApiService
import com.stephenbain.lines.api.TopicJson
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
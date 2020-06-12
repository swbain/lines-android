package com.stephenbain.lines.common.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(val topic_list: TopicList)

@JsonClass(generateAdapter = true)
data class TopicList(val topics: List<Topic>)

@JsonClass(generateAdapter = true)
data class Topic(val id: Long, val title: String)
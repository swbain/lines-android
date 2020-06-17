package com.stephenbain.lines.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(val topic_list: TopicList)

@JsonClass(generateAdapter = true)
data class TopicList(val topics: List<TopicJson>)

@JsonClass(generateAdapter = true)
data class TopicJson(val id: Long, val title: String)
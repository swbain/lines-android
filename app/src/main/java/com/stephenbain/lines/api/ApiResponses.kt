package com.stephenbain.lines.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(@Json(name = "topic_list") val topicList: TopicList)

@JsonClass(generateAdapter = true)
data class TopicList(val topics: List<TopicJson>)

@JsonClass(generateAdapter = true)
data class TopicJson(val id: Long, val title: String)
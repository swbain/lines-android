package com.stephenbain.lines.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(@Json(name = "topic_list") val topicList: TopicList)

@JsonClass(generateAdapter = true)
data class TopicList(
    val topics: List<Topic>,
    @Json(name = "more_topics_url") val moreTopicsUrl: String?
)

@JsonClass(generateAdapter = true)
data class Topic(val id: Long, val title: String, @Json(name = "category_id") val categoryId: Long)

@JsonClass(generateAdapter = true)
data class CategoriesResponse(@Json(name = "category_list") val categoryList: CategoryList)

@JsonClass(generateAdapter = true)
data class CategoryList(val categories: List<Category>)

@JsonClass(generateAdapter = true)
data class Category(val id: Long, val name: String)
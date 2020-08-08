package com.stephenbain.lines.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(
    @Json(name = "topic_list") val topicList: TopicList,
    val users: List<User>
)

@JsonClass(generateAdapter = true)
data class TopicList(
    val topics: List<TopicJson>,
    @Json(name = "more_topics_url") val moreTopicsUrl: String?
)

interface Topic {
    val id: Long
    val title: String
    val categoryId: Long
    val posters: List<Poster>
    val postCount: Int
}

@JsonClass(generateAdapter = true)
data class Poster(@Json(name = "user_id") val userId: Long)

@JsonClass(generateAdapter = true)
data class TopicJson(
    override val id: Long,
    override val title: String,
    @Json(name = "category_id") override val categoryId: Long,
    override val posters: List<Poster>,
    @Json(name = "posts_count") override val postCount: Int
) : Topic

@JsonClass(generateAdapter = true)
data class CategoriesResponse(@Json(name = "category_list") val categoryList: CategoryList)

@JsonClass(generateAdapter = true)
data class CategoryList(val categories: List<Category>)

@JsonClass(generateAdapter = true)
data class Category(
    val id: Long,
    val name: String,
    val position: Int,
    @Json(name="topic_count") val topicCount: Int,
    val color: String
)

@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    val username: String,
    val name: String?,
    @Json(name = "avatar_template") val avatarTemplate: String
)

fun User.avatarUrl(size: Int): String {
    return "$BASE_URL${avatarTemplate.replace("{size}", "$size")}"
}
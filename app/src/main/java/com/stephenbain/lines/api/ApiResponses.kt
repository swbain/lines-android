package com.stephenbain.lines.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class GetLatestApiResponse(
    @Json(name = "topic_list") val topicList: TopicList,
    val users: List<User>
)

@JsonClass(generateAdapter = true)
data class TopicList(
    val topics: List<Topic>,
    @Json(name = "more_topics_url") val moreTopicsUrl: String?
)

interface Topic {
    val id: Long
    val title: String
    val categoryId: Long
    val posters: List<Poster>
    val postCount: Int
    val views: Int
    val lastPostedAt: Date
}

@JsonClass(generateAdapter = true)
data class Poster(@Json(name = "user_id") val userId: Long)

@JsonClass(generateAdapter = true)
data class TopicJson(
    val id: Long,
    val title: String,
    @Json(name = "category_id") val categoryId: Long,
    val posters: List<Poster>,
    @Json(name = "posts_count") val postCount: Int,
    val views: Int,
    @Json(name="bumped_at") val lastPostedAt: String
)

data class TopicImpl(
    override val id: Long,
    override val title: String,
    override val categoryId: Long,
    override val posters: List<Poster>,
    override val postCount: Int,
    override val views: Int,
    override val lastPostedAt: Date
) : Topic

@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    val username: String,
    val name: String?,
    @Json(name = "avatar_template") val avatarTemplate: String
)
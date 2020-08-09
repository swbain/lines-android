package com.stephenbain.lines.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class TopicJsonAdapter {

    private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    @FromJson
    fun topicFromJson(topicJson: TopicJson): Topic = TopicImpl(
        topicJson.id,
        topicJson.title,
        topicJson.categoryId,
        topicJson.posters,
        topicJson.postCount,
        topicJson.views,
        topicJson.lastPostedAt.toDate()
    )

    @ToJson
    fun topicToJson(topic: Topic): TopicJson = TopicJson(
        topic.id,
        topic.title,
        topic.categoryId,
        topic.posters,
        topic.postCount,
        topic.views,
        topic.lastPostedAt.toFormattedString()
    )

    private fun String.toDate(): Date {
        return dateFormat.parse(this) ?: throw IllegalStateException("couldn't parse date")
    }

    private fun Date.toFormattedString(): String {
        return dateFormat.format(this)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}